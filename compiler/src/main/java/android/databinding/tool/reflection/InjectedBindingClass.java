/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.databinding.tool.reflection;

import android.databinding.tool.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A class that can be used by ModelAnalyzer without any backing model. This is used
 * for ViewDataBinding subclasses that haven't been generated yet, but we still want
 * to resolve methods and fields for them.
 *
 * @see ModelAnalyzer#injectViewDataBinding(String, Map, Map)
 */
public class InjectedBindingClass extends ModelClass {
    private final String mClassName;
    private final String mSuperClass;
    private final Map<String, String> mVariables;
    private final Map<String, String> mFields;

    public InjectedBindingClass(String className, String superClass, Map<String, String> variables,
            Map<String, String> fields) {
        mClassName = className;
        mSuperClass = superClass;
        mVariables = variables;
        mFields = fields;
    }

    @Override
    public String toJavaCode() {
        return mClassName;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public ModelClass getComponentType() {
        return null;
    }

    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isChar() {
        return false;
    }

    @Override
    public boolean isByte() {
        return false;
    }

    @Override
    public boolean isShort() {
        return false;
    }

    @Override
    public boolean isInt() {
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean isFloat() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isGeneric() {
        return false;
    }

    @Override
    public List<ModelClass> getTypeArguments() {
        return null;
    }

    @Override
    public boolean isTypeVar() {
        return false;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public ModelClass unbox() {
        return this;
    }

    @Override
    public ModelClass box() {
        return this;
    }

    @Override
    public boolean isObservable() {
        return getSuperclass().isObservable();
    }

    @Override
    public boolean isAssignableFrom(ModelClass that) {
        ModelClass superClass = that;
        while (superClass != null && !superClass.isObject()) {
            if (superClass.toJavaCode().equals(mClassName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ModelClass getSuperclass() {
        return ModelAnalyzer.getInstance().findClass(mSuperClass, null);
    }

    @Override
    public ModelClass erasure() {
        return this;
    }

    @Override
    public String getJniDescription() {
        return TypeUtil.getInstance().getDescription(this);
    }

    @Override
    protected ModelField[] getDeclaredFields() {
        ModelClass superClass = getSuperclass();
        final ModelField[] superFields = superClass.getDeclaredFields();
        final int fieldCount = superFields.length + mFields.size();
        final ModelField[] fields = Arrays.copyOf(superFields, fieldCount);
        int index = superFields.length;
        for (String fieldName : mFields.keySet()) {
            final String fieldType = mFields.get(fieldName);
            fields[index++] = new InjectedBindingClassField(fieldName, fieldType);
        }
        return fields;
    }

    @Override
    protected ModelMethod[] getDeclaredMethods() {
        ModelClass superClass = getSuperclass();
        final ModelMethod[] superMethods = superClass.getDeclaredMethods();
        final int methodCount = superMethods.length + (mVariables.size() * 2);
        final ModelMethod[] methods = Arrays.copyOf(superMethods, methodCount);
        int index = superMethods.length;
        for (String variableName : mVariables.keySet()) {
            final String variableType = mVariables.get(variableName);
            final String getterName = "get" + StringUtils.capitalize(variableName);
            methods[index++] = new InjectedBindingClassMethod(this, getterName, variableType, null);
            final String setterName = "set" + StringUtils.capitalize(variableName);
            methods[index++] = new InjectedBindingClassMethod(this, setterName, "void", variableType);
        }
        return methods;
    }

    @Override
    public String toString() {
        return "Injected Class: " + mClassName;
    }
}
