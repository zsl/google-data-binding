/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.databinding.annotationprocessor;

import android.databinding.Bindable;
import android.databinding.tool.CompilerChef.BindableHolder;
import android.databinding.tool.DataBindingCompilerArgs;
import android.databinding.tool.util.GenerationalClassUtil;
import android.databinding.tool.util.L;
import android.databinding.tool.util.LoggedErrorException;
import android.databinding.tool.util.Preconditions;
import android.databinding.tool.writer.BRWriter;
import android.databinding.tool.writer.JavaFileWriter;

import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;

// binding app info and library info are necessary to trigger this.
public class ProcessBindable extends ProcessDataBinding.ProcessingStep implements BindableHolder {
    private Intermediate mProperties;
    private HashMap<String, HashSet<String>> mLayoutVariables = new HashMap<>();

    @Override
    public boolean onHandleStep(RoundEnvironment roundEnv, ProcessingEnvironment processingEnv,
            DataBindingCompilerArgs args) {
        if (mProperties == null) {
            mProperties = new IntermediateV1(args.getModulePackage());
            mergeLayoutVariables();
            mLayoutVariables.clear();
            TypeElement observableType = processingEnv.getElementUtils().
                    getTypeElement("android.databinding.Observable");
            Types typeUtils = processingEnv.getTypeUtils();
            for (Element element : AnnotationUtil
                    .getElementsAnnotatedWith(roundEnv, Bindable.class)) {
                try {
                    Element enclosingElement = element.getEnclosingElement();
                    ElementKind kind = enclosingElement.getKind();
                    if (kind != ElementKind.CLASS && kind != ElementKind.INTERFACE) {
                        L.e("Bindable must be on a member field or method. The enclosing type is %s",
                                enclosingElement.getKind());
                    }
                    TypeElement enclosing = (TypeElement) enclosingElement;
                    if (!typeUtils.isAssignable(enclosing.asType(), observableType.asType())) {
                        L.e("Bindable must be on a member in an Observable class. %s is not Observable",
                                enclosingElement.getSimpleName());
                    }
                    String name = getPropertyName(element);
                    if (name != null) {
                        Preconditions
                                .checkNotNull(mProperties, "Must receive app / library info before "
                                        + "Bindable fields.");
                        mProperties.addProperty(enclosing.getQualifiedName().toString(), name);
                    }
                } catch (LoggedErrorException e) {
                    // We'll get them later when we do the messages
                }
            }
            GenerationalClassUtil.writeIntermediateFile(mProperties.getPackage(),
                    createIntermediateFileName(mProperties.getPackage()), mProperties);
            generateBRClasses(args, mProperties.getPackage());
        }
        return false;
    }

    @Override
    public void addVariable(String variableName, String containingClassName) {
        HashSet<String> variableNames = mLayoutVariables.get(containingClassName);
        if (variableNames == null) {
            variableNames = new HashSet<String>();
            mLayoutVariables.put(containingClassName, variableNames);
        }
        variableNames.add(variableName);
    }

    @Override
    public void onProcessingOver(RoundEnvironment roundEnvironment,
            ProcessingEnvironment processingEnvironment, DataBindingCompilerArgs args) {
    }

    private String createIntermediateFileName(String appPkg) {
        return appPkg + GenerationalClassUtil.ExtensionFilter.BR.getExtension();
    }

    private void generateBRClasses(DataBindingCompilerArgs compilerArgs, String pkg) {
        try {
            Set<String> written = new HashSet<>();
            DataBindingCompilerArgs.Type artifactType = compilerArgs.artifactType();
            L.d("************* Generating BR file %s. use final: %s", pkg, artifactType.name());
            HashSet<String> properties = new HashSet<>();
            mProperties.captureProperties(properties);
            Collection<Intermediate> previousIntermediates = loadPreviousBRFiles(pkg + ".BR");
            for (Intermediate intermediate : previousIntermediates) {
                intermediate.captureProperties(properties);
            }
            final JavaFileWriter writer = getWriter();
            boolean useFinal = artifactType == DataBindingCompilerArgs.Type.APPLICATION
                    || compilerArgs.isTestVariant();
            BRWriter brWriter = new BRWriter(properties, useFinal);
            // bazel has duplicate package names so we need to avoid overwriting BR files.
            writer.writeToFile(pkg + ".BR", brWriter.write(pkg));
            written.add(pkg);
            List<String> brPackages = new ArrayList<>();
            brPackages.add(pkg);
            for (Intermediate intermediate : previousIntermediates) {
                brPackages.add(intermediate.getPackage());
            }
            if (!compilerArgs.isTestVariant() || compilerArgs.isLibrary()) {
                // Generate BR for all previous packages.
                for (Intermediate intermediate : previousIntermediates) {
                    if (written.add(intermediate.getPackage())) {
                        writer.writeToFile(intermediate.getPackage() + ".BR",
                                brWriter.write(intermediate.getPackage()));
                    }
                }
            }
            mCallback.onBrWriterReady(brWriter, brPackages);
        } catch (LoggedErrorException e) {
            // This will be logged later
        }
    }

    private String getPropertyName(Element element) {
        switch (element.getKind()) {
            case FIELD:
                return stripPrefixFromField((VariableElement) element);
            case METHOD:
                return stripPrefixFromMethod((ExecutableElement) element);
            default:
                L.e("@Bindable is not allowed on %s", element.getKind());
                return null;
        }
    }

    private static String stripPrefixFromField(VariableElement element) {
        Name name = element.getSimpleName();
        if (name.length() >= 2) {
            char firstChar = name.charAt(0);
            char secondChar = name.charAt(1);
            if (name.length() > 2 && firstChar == 'm' && secondChar == '_') {
                char thirdChar = name.charAt(2);
                if (Character.isJavaIdentifierStart(thirdChar)) {
                    return "" + Character.toLowerCase(thirdChar) +
                            name.subSequence(3, name.length());
                }
            } else if ((firstChar == 'm' && Character.isUpperCase(secondChar)) ||
                    (firstChar == '_' && Character.isJavaIdentifierStart(secondChar))) {
                return "" + Character.toLowerCase(secondChar) + name.subSequence(2, name.length());
            }
        }
        return name.toString();
    }

    private String stripPrefixFromMethod(ExecutableElement element) {
        Name name = element.getSimpleName();
        CharSequence propertyName;
        if (isGetter(element) || isSetter(element)) {
            propertyName = name.subSequence(3, name.length());
        } else if (isBooleanGetter(element)) {
            propertyName = name.subSequence(2, name.length());
        } else {
            L.e("@Bindable associated with method must follow JavaBeans convention %s", element);
            return null;
        }
        char firstChar = propertyName.charAt(0);
        return "" + Character.toLowerCase(firstChar) +
                propertyName.subSequence(1, propertyName.length());
    }

    private void mergeLayoutVariables() {
        for (String containingClass : mLayoutVariables.keySet()) {
            for (String variable : mLayoutVariables.get(containingClass)) {
                mProperties.addProperty(containingClass, variable);
            }
        }
    }

    private static boolean prefixes(CharSequence sequence, String prefix) {
        boolean prefixes = false;
        if (sequence.length() > prefix.length()) {
            int count = prefix.length();
            prefixes = true;
            for (int i = 0; i < count; i++) {
                if (sequence.charAt(i) != prefix.charAt(i)) {
                    prefixes = false;
                    break;
                }
            }
        }
        return prefixes;
    }

    private static boolean isGetter(ExecutableElement element) {
        Name name = element.getSimpleName();
        return prefixes(name, "get") &&
                Character.isJavaIdentifierStart(name.charAt(3)) &&
                element.getParameters().isEmpty() &&
                element.getReturnType().getKind() != TypeKind.VOID;
    }

    private static boolean isSetter(ExecutableElement element) {
        Name name = element.getSimpleName();
        return prefixes(name, "set") &&
                Character.isJavaIdentifierStart(name.charAt(3)) &&
                element.getParameters().size() == 1 &&
                element.getReturnType().getKind() == TypeKind.VOID;
    }

    private static boolean isBooleanGetter(ExecutableElement element) {
        Name name = element.getSimpleName();
        return prefixes(name, "is") &&
                Character.isJavaIdentifierStart(name.charAt(2)) &&
                element.getParameters().isEmpty() &&
                element.getReturnType().getKind() == TypeKind.BOOLEAN;
    }

    @SuppressWarnings("CodeBlock2Expr")
    private Collection<Intermediate> loadPreviousBRFiles(String... excludePackages) {
        List<Intermediate> brFiles = GenerationalClassUtil
                .loadObjects(GenerationalClassUtil.ExtensionFilter.BR);
        Set<String> excludeMap = Sets.newHashSet(excludePackages);
        // dedupe
        Map<String, Intermediate> items = new HashMap<>();
        brFiles.stream()
                .filter(intermediate -> !excludeMap.contains(intermediate.getPackage()))
                .forEach(intermediate -> {
                    items.put(intermediate.getPackage(), intermediate);
                });
        return items.values();
    }

    private interface Intermediate extends Serializable {

        void captureProperties(Set<String> properties);

        void addProperty(String className, String propertyName);

        boolean hasValues();

        String getPackage();
    }

    private static class IntermediateV1 implements Serializable, Intermediate {
        private static final long serialVersionUID = 2L;

        private String mPackage;
        private final HashMap<String, HashSet<String>> mProperties = new HashMap<>();

        public IntermediateV1(String aPackage) {
            mPackage = aPackage;
        }

        @Override
        public void captureProperties(Set<String> properties) {
            for (HashSet<String> propertySet : mProperties.values()) {
                properties.addAll(propertySet);
            }
        }

        @Override
        public void addProperty(String className, String propertyName) {
            HashSet<String> properties = mProperties.get(className);
            if (properties == null) {
                properties = new HashSet<String>();
                mProperties.put(className, properties);
            }
            properties.add(propertyName);
        }

        @Override
        public boolean hasValues() {
            return !mProperties.isEmpty();
        }

        @Override
        public String getPackage() {
            return mPackage;
        }
    }
}
