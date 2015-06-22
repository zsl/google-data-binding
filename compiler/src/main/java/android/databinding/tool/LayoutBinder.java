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

package android.databinding.tool;

import com.google.common.base.Preconditions;

import android.databinding.tool.expr.Dependency;
import android.databinding.tool.expr.Expr;
import android.databinding.tool.expr.ExprModel;
import android.databinding.tool.expr.IdentifierExpr;
import android.databinding.tool.store.ResourceBundle;
import android.databinding.tool.store.ResourceBundle.BindingTargetBundle;
import android.databinding.tool.writer.LayoutBinderWriter;
import android.databinding.tool.writer.WriterPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps all information about the bindings per layout file
 */
public class LayoutBinder {
    private static final Comparator<BindingTarget> COMPARE_FIELD_NAME = new Comparator<BindingTarget>() {
        @Override
        public int compare(BindingTarget first, BindingTarget second) {
            final String fieldName1 = WriterPackage.getFieldName(first);
            final String fieldName2 = WriterPackage.getFieldName(second);
            return fieldName1.compareTo(fieldName2);
        }
    };

    /*
    * val pkg: String, val projectPackage: String, val baseClassName: String,
        val layoutName:String, val lb: LayoutExprBinding*/
    private final ExprModel mExprModel;
    private final ExpressionParser mExpressionParser;
    private final List<BindingTarget> mBindingTargets;
    private final List<BindingTarget> mSortedBindingTargets;
    private String mModulePackage;
    private final HashMap<String, String> mUserDefinedVariables = new HashMap<String, String>();

    private LayoutBinderWriter mWriter;
    private ResourceBundle.LayoutFileBundle mBundle;
    private static final String[] sJavaLangClasses = {
            "Deprecated",
            "Override",
            "SafeVarargs",
            "SuppressWarnings",
            "Appendable",
            "AutoCloseable",
            "CharSequence",
            "Cloneable",
            "Comparable",
            "Iterable",
            "Readable",
            "Runnable",
            "Thread.UncaughtExceptionHandler",
            "Boolean",
            "Byte",
            "Character",
            "Character.Subset",
            "Character.UnicodeBlock",
            "Class",
            "ClassLoader",
            "Compiler",
            "Double",
            "Enum",
            "Float",
            "InheritableThreadLocal",
            "Integer",
            "Long",
            "Math",
            "Number",
            "Object",
            "Package",
            "Process",
            "ProcessBuilder",
            "Runtime",
            "RuntimePermission",
            "SecurityManager",
            "Short",
            "StackTraceElement",
            "StrictMath",
            "String",
            "StringBuffer",
            "StringBuilder",
            "System",
            "Thread",
            "ThreadGroup",
            "ThreadLocal",
            "Throwable",
            "Void",
            "Thread.State",
            "ArithmeticException",
            "ArrayIndexOutOfBoundsException",
            "ArrayStoreException",
            "ClassCastException",
            "ClassNotFoundException",
            "CloneNotSupportedException",
            "EnumConstantNotPresentException",
            "Exception",
            "IllegalAccessException",
            "IllegalArgumentException",
            "IllegalMonitorStateException",
            "IllegalStateException",
            "IllegalThreadStateException",
            "IndexOutOfBoundsException",
            "InstantiationException",
            "InterruptedException",
            "NegativeArraySizeException",
            "NoSuchFieldException",
            "NoSuchMethodException",
            "NullPointerException",
            "NumberFormatException",
            "ReflectiveOperationException",
            "RuntimeException",
            "SecurityException",
            "StringIndexOutOfBoundsException",
            "TypeNotPresentException",
            "UnsupportedOperationException",
            "AbstractMethodError",
            "AssertionError",
            "ClassCircularityError",
            "ClassFormatError",
            "Error",
            "ExceptionInInitializerError",
            "IllegalAccessError",
            "IncompatibleClassChangeError",
            "InstantiationError",
            "InternalError",
            "LinkageError",
            "NoClassDefFoundError",
            "NoSuchFieldError",
            "NoSuchMethodError",
            "OutOfMemoryError",
            "StackOverflowError",
            "ThreadDeath",
            "UnknownError",
            "UnsatisfiedLinkError",
            "UnsupportedClassVersionError",
            "VerifyError",
            "VirtualMachineError",
    };

    public LayoutBinder(ResourceBundle.LayoutFileBundle layoutBundle) {
        mExprModel = new ExprModel();
        mExpressionParser = new ExpressionParser(mExprModel);
        mBindingTargets = new ArrayList<BindingTarget>();
        mBundle = layoutBundle;
        mModulePackage = layoutBundle.getModulePackage();
        // copy over data.
        for (Map.Entry<String, String> variable : mBundle.getVariables().entrySet()) {
            addVariable(variable.getKey(), variable.getValue());
        }

        for (Map.Entry<String, String> userImport : mBundle.getImports().entrySet()) {
            mExprModel.addImport(userImport.getKey(), userImport.getValue());
        }
        for (String javaLangClass : sJavaLangClasses) {
            mExprModel.addImport(javaLangClass, "java.lang." + javaLangClass);
        }
        for (BindingTargetBundle targetBundle : mBundle.getBindingTargetBundles()) {
            final BindingTarget bindingTarget = createBindingTarget(targetBundle);
            for (ResourceBundle.BindingTargetBundle.BindingBundle bindingBundle : targetBundle
                    .getBindingBundleList()) {
                bindingTarget.addBinding(bindingBundle.getName(), parse(bindingBundle.getExpr()));
            }
            bindingTarget.resolveMultiSetters();
        }
        mSortedBindingTargets = new ArrayList<BindingTarget>(mBindingTargets);
        Collections.sort(mSortedBindingTargets, COMPARE_FIELD_NAME);
    }

    public void resolveWhichExpressionsAreUsed() {
        List<Expr> used = new ArrayList<Expr>();
        for (BindingTarget target : mBindingTargets) {
            for (Binding binding : target.getBindings()) {
                binding.getExpr().setIsUsed(true);
                used.add(binding.getExpr());
            }
        }
        while (!used.isEmpty()) {
            Expr e = used.remove(used.size() - 1);
            for (Dependency dep : e.getDependencies()) {
                if (!dep.getOther().isUsed()) {
                    used.add(dep.getOther());
                    dep.getOther().setIsUsed(true);
                }
            }
        }
    }

    public IdentifierExpr addVariable(String name, String type) {
        Preconditions.checkState(!mUserDefinedVariables.containsKey(name),
                "%s has already been defined as %s", name, type);
        final IdentifierExpr id = mExprModel.identifier(name);
        id.setUserDefinedType(type);
        id.enableDirectInvalidation();
        mUserDefinedVariables.put(name, type);
        return id;
    }

    public HashMap<String, String> getUserDefinedVariables() {
        return mUserDefinedVariables;
    }

    public BindingTarget createBindingTarget(ResourceBundle.BindingTargetBundle targetBundle) {
        final BindingTarget target = new BindingTarget(targetBundle);
        mBindingTargets.add(target);
        target.setModel(mExprModel);
        return target;
    }

    public Expr parse(String input) {
        final Expr parsed = mExpressionParser.parse(input);
        parsed.setBindingExpression(true);
        return parsed;
    }

    public List<BindingTarget> getBindingTargets() {
        return mBindingTargets;
    }

    public List<BindingTarget> getSortedTargets() {
        return mSortedBindingTargets;
    }

    public boolean isEmpty() {
        return mExprModel.size() == 0;
    }

    public ExprModel getModel() {
        return mExprModel;
    }

    private void ensureWriter() {
        if (mWriter == null) {
            mWriter = new LayoutBinderWriter(this);
        }
    }

    public String writeViewBinderBaseClass(boolean forLibrary) {
        ensureWriter();
        return mWriter.writeBaseClass(forLibrary);
    }


    public String writeViewBinder(int minSdk) {
        mExprModel.seal();
        ensureWriter();
        Preconditions.checkNotNull(getPackage(), "package cannot be null");
        Preconditions.checkNotNull(getClassName(), "base class name cannot be null");
        return mWriter.write(minSdk);
    }

    public String getPackage() {
        return mBundle.getBindingClassPackage();
    }

    public boolean isMerge() {
        return mBundle.isMerge();
    }

    public String getModulePackage() {
        return mModulePackage;
    }

    public String getLayoutname() {
        return mBundle.getFileName();
    }

    public String getImplementationName() {
        if (hasVariations()) {
            return mBundle.getBindingClassName() + mBundle.getConfigName() + "Impl";
        } else {
            return mBundle.getBindingClassName();
        }
    }
    
    public String getClassName() {
        return mBundle.getBindingClassName();
    }

    public String getTag() {
        return mBundle.getDirectory() + "/" + mBundle.getFileName();
    }

    public boolean hasVariations() {
        return mBundle.hasVariations();
    }
}
