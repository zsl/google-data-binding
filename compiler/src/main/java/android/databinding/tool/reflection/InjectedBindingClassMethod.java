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

import java.util.List;
import java.util.Map;

/**
 * A class that can be used by ModelAnalyzer without any backing model. This is used
 * for methods on ViewDataBinding subclasses that haven't been generated yet.
 *
 * @see ModelAnalyzer#injectViewDataBinding(String, Map, Map)
 */
public class InjectedBindingClassMethod extends ModelMethod {
    private final InjectedBindingClass mContainingClass;
    private final String mName;
    private final String mReturnType;
    private final String mParameter;

    public InjectedBindingClassMethod(InjectedBindingClass containingClass, String name, String returnType,
            String parameter) {
        mContainingClass = containingClass;
        mName = name;
        mReturnType = returnType;
        mParameter = parameter;
    }

    @Override
    public ModelClass getDeclaringClass() {
        return mContainingClass;
    }

    @Override
    public ModelClass[] getParameterTypes() {
        if (mParameter != null) {
            ModelClass parameterType = ModelAnalyzer.getInstance().findClass(mParameter, null);
            return new ModelClass[] { parameterType };
        }
        return new ModelClass[0];
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public ModelClass getReturnType(List<ModelClass> args) {
        ModelClass returnType = ModelAnalyzer.getInstance().findClass(mReturnType, null);
        return returnType;
    }

    @Override
    public boolean isVoid() {
        return getReturnType().isVoid();
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isAbstract() {
        return true;
    }

    @Override
    public boolean isBindable() {
        return false;
    }

    @Override
    public int getMinApi() {
        return 0;
    }

    @Override
    public String getJniDescription() {
        return TypeUtil.getInstance().getDescription(this);
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }
}
