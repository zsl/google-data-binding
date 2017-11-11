/*
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.databinding.tool;

import android.databinding.tool.expr.ExprModel;
import android.databinding.tool.reflection.InjectedClass;
import android.databinding.tool.reflection.InjectedMethod;
import android.databinding.tool.reflection.ModelAnalyzer;
import android.databinding.tool.reflection.ModelClass;
import android.databinding.tool.store.ResourceBundle;
import android.databinding.tool.util.L;
import android.databinding.tool.util.Preconditions;
import android.databinding.tool.writer.BRWriter;
import android.databinding.tool.writer.BindingMapperWriter;
import android.databinding.tool.writer.DynamicUtilWriter;
import android.databinding.tool.writer.JavaFileWriter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Chef class for compiler.
 *
 * Different build systems can initiate a version of this to handle their work
 */
public class CompilerChef {
    private static final String[] VERSION_CODES = {
            "BASE",                 // 1
            "BASE_1_1",             // 2
            "CUPCAKE",              // 3
            "DONUT",                // 4
            "ECLAIR",               // 5
            "ECLAIRE_0_1",          // 6
            "ECLAIR_MR1",           // 7
            "FROYO",                // 8
            "GINGERBREAD",          // 9
            "GINGERBREAD_MR1",      // 10
            "HONEYCOMB",            // 11
            "HONEYCOMB_MR1",        // 12
            "HONEYCOMB_MR2",        // 13
            "ICE_CREAM_SANDWICH",   // 14
            "ICE_CREAM_SANDWICH_MR1",// 15
            "JELLY_BEAN",           // 16
            "JELLY_BEAN_MR1",       // 17
            "JELLY_BEAN_MR2",       // 18
            "KITKAT",               // 19
            "KITKAT_WATCH",         // 20
            "LOLLIPOP",             // 21
            "LOLLIPOP_MR1",         // 22
            "M",                    // 23
    };
    private JavaFileWriter mFileWriter;
    private ResourceBundle mResourceBundle;
    private DataBinder mDataBinder;
    private boolean mEnableV2;

    private CompilerChef() {
    }

    public static CompilerChef createChef(ResourceBundle bundle, JavaFileWriter fileWriter,
            DataBindingCompilerArgs compilerArgs) {
        CompilerChef chef = new CompilerChef();

        chef.mResourceBundle = bundle;
        chef.mFileWriter = fileWriter;
        chef.mResourceBundle.validateMultiResLayouts();
        chef.mEnableV2 = compilerArgs.isEnableV2();
        chef.pushClassesToAnalyzer();
        chef.pushDynamicUtilToAnalyzer();
        return chef;
    }

    public ResourceBundle getResourceBundle() {
        return mResourceBundle;
    }

    public void ensureDataBinder() {
        if (mDataBinder == null) {
            mDataBinder = new DataBinder(mResourceBundle, mEnableV2);
            mDataBinder.setFileWriter(mFileWriter);
        }
    }

    public boolean hasAnythingToGenerate() {
        L.d("checking if we have anything to generate. bundle size: %s",
                mResourceBundle == null ? -1 : mResourceBundle.getLayoutBundles().size());
        return mResourceBundle != null && mResourceBundle.getLayoutBundles().size() > 0;
    }

    /**
     * Injects ViewDataBinding subclasses to the ModelAnalyzer so that they can be
     * analyzed prior to creation. This is useful for resolving variable setters and
     * View fields during compilation.
     */
    private void pushClassesToAnalyzer() {
        ModelAnalyzer analyzer = ModelAnalyzer.getInstance();
        for (String layoutName : mResourceBundle.getLayoutBundles().keySet()) {
            final HashSet<String> variables = new HashSet();
            final HashSet<String> fields = new HashSet();

            List<ResourceBundle.LayoutFileBundle> bundles =
                    mResourceBundle.getLayoutBundles().get(layoutName);
            final String className = bundles.get(0).getBindingClassPackage() + "."
                    + bundles.get(0).getBindingClassName();
            // inject base class
            InjectedClass bindingClass =
                    new InjectedClass(className, ModelAnalyzer.VIEW_DATA_BINDING);
            analyzer.injectClass(bindingClass);

            for (ResourceBundle.LayoutFileBundle layoutFileBundle : bundles) {
                final HashMap<String, String> imports = new HashMap<String, String>();
                for (ResourceBundle.NameTypeLocation imp : layoutFileBundle.getImports()) {
                    imports.put(imp.name, imp.type);
                }

                for (ResourceBundle.VariableDeclaration variable :
                        layoutFileBundle.getVariables()) {
                    if (variables.add(variable.name)) {
                        bindingClass.addVariable(variable.name, variable.type, imports);
                    }
                }

                for (ResourceBundle.BindingTargetBundle bindingTargetBundle :
                        layoutFileBundle.getBindingTargetBundles()) {
                    if (bindingTargetBundle.getId() != null) {
                        String fieldName = bindingTargetBundle.getId();
                        if (fields.add(fieldName)) {
                            String fieldType = bindingTargetBundle.getInterfaceType();
                            bindingClass.addField(fieldName, fieldType);
                        }
                    }
                }
                // inject implementation
                if (mEnableV2 || bundles.size() > 1) {
                    // Add the implementation class
                    final String implName = className + layoutFileBundle.getConfigName() + "Impl";
                    analyzer.injectClass(new InjectedClass(implName, className));
                }
            }
        }
    }

    public static InjectedClass pushDynamicUtilToAnalyzer() {
        InjectedClass injectedClass = new InjectedClass("android.databinding.DynamicUtil",
                "java.lang.Object");
        for (Map.Entry<Class, Class> entry : ModelClass.BOX_MAPPING.entrySet()) {
            injectedClass.addMethod(new InjectedMethod(injectedClass, true,
                    ExprModel.SAFE_UNBOX_METHOD_NAME, null, entry.getKey().getCanonicalName(),
                    entry.getValue().getCanonicalName()));
        }

        ModelAnalyzer analyzer = ModelAnalyzer.getInstance();
        analyzer.injectClass(injectedClass);
        return injectedClass;
    }

    public void writeDataBinderMapper(DataBindingCompilerArgs compilerArgs, BRWriter brWriter) {
        ensureDataBinder();
        final String pkg = "android.databinding";
        BindingMapperWriter dbr = new BindingMapperWriter(pkg, "DataBinderMapper",
                mDataBinder.getLayoutBinders(), compilerArgs);
        mFileWriter.writeToFile(pkg + "." + dbr.getClassName(), dbr.write(brWriter));
    }

    public void writeDynamicUtil() {
        DynamicUtilWriter dynamicUtil = new DynamicUtilWriter();
        // TODO: Replace this with targetSDK check from plugin
        ModelClass versionCodes = ModelAnalyzer.getInstance().findClass(
                "android.os.Build.VERSION_CODES", null);
        Preconditions.checkNotNull(versionCodes, "Could not find compile SDK");
        int compileVersion = 1;
        for (int i = VERSION_CODES.length - 1; i >= 0; i--) {
            if (versionCodes.findGetterOrField(VERSION_CODES[i], true) != null) {
                compileVersion = i + 1;
                break;
            }
        }
        mFileWriter.writeToFile("android.databinding.DynamicUtil",
                dynamicUtil.write(compileVersion).generate());
    }

    /**
     * Adds variables to list of Bindables.
     */
    public void addBRVariables(BindableHolder bindables) {
        ensureDataBinder();
        for (LayoutBinder layoutBinder : mDataBinder.mLayoutBinders) {
            for (String variableName : layoutBinder.getUserDefinedVariables().keySet()) {
                bindables.addVariable(variableName, layoutBinder.getClassName());
            }
        }
    }

    public void sealModels() {
        ensureDataBinder();
        mDataBinder.sealModels();
    }

    public void writeViewBinderInterfaces(boolean isLibrary) {
        ensureDataBinder();
        mDataBinder.writerBaseClasses(isLibrary);
    }

    public void writeViewBinders(int minSdk) {
        ensureDataBinder();
        mDataBinder.writeBinders(minSdk);
    }

    public void writeComponent() {
        ensureDataBinder();
        mDataBinder.writeComponent();
    }

    public Set<String> getWrittenClassNames() {
        ensureDataBinder();
        return mDataBinder.getWrittenClassNames();
    }

    public interface BindableHolder {
        void addVariable(String variableName, String containingClassName);
    }
}
