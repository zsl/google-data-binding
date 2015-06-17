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

import android.databinding.tool.expr.Expr;
import android.databinding.tool.reflection.ModelAnalyzer;
import android.databinding.tool.reflection.ModelClass;
import android.databinding.tool.store.SetterStore;
import android.databinding.tool.store.SetterStore.SetterCall;
import android.databinding.tool.writer.CodeGenUtil;
import android.databinding.tool.writer.FlagSet;
import android.databinding.tool.writer.WriterPackage;

public class Binding {

    private final String mName;
    private final Expr mExpr;
    private final BindingTarget mTarget;
    private SetterStore.SetterCall mSetterCall;

    public Binding(BindingTarget target, String name, Expr expr) {
        mTarget = target;
        mName = name;
        mExpr = expr;
    }

    private SetterStore.BindingSetterCall getSetterCall() {
        if (mSetterCall == null) {
            ModelClass viewType = mTarget.getResolvedType();
            if (viewType != null && viewType.extendsViewStub()) {
                if (isViewStubAttribute()) {
                    mSetterCall = new ViewStubDirectCall(mName, viewType, mExpr);
                } else {
                    mSetterCall = new ViewStubSetterCall(mName);
                }
            } else {
                mSetterCall = SetterStore.get(ModelAnalyzer.getInstance()).getSetterCall(mName,
                        viewType, mExpr.getResolvedType(), mExpr.getModel().getImports());
            }
        }
        return mSetterCall;
    }

    public BindingTarget getTarget() {
        return mTarget;
    }

    public String toJavaCode(String targetViewName) {
        final String currentValue = requiresOldValue()
                ? "this." + WriterPackage.getOldValueName(mExpr) : null;
        final String argCode = CodeGenUtil.Companion.toCode(getExpr(), false).generate();
        return getSetterCall().toJava(targetViewName, currentValue, argCode);
    }

    public Expr[] getComponentExpressions() {
        return new Expr[] { mExpr };
    }

    public boolean requiresOldValue() {
        return getSetterCall().requiresOldValue();
    }

    /**
     * The min api level in which this binding should be executed.
     * <p>
     * This should be the minimum value among the dependencies of this binding. For now, we only
     * check the setter.
     */
    public int getMinApi() {
        return getSetterCall().getMinApi();
    }

    public String getName() {
        return mName;
    }

    public Expr getExpr() {
        return mExpr;
    }

    private boolean isViewStubAttribute() {
        if ("android:inflatedId".equals(mName)) {
            return true;
        } else if ("android:layout".equals(mName)) {
            return true;
        } else if ("android:visibility".equals(mName)) {
            return true;
        } else {
            return false;
        }
    }

    private static class ViewStubSetterCall extends SetterCall {
        private final String mName;

        public ViewStubSetterCall(String name) {
            mName = name.substring(name.lastIndexOf(':') + 1);
        }

        @Override
        protected String toJavaInternal(String viewExpression, String converted) {
            return "if (" + viewExpression + ".isInflated()) " + viewExpression +
                    ".getBinding().setVariable(BR." + mName + ", " + converted + ")";
        }

        @Override
        protected String toJavaInternal(String viewExpression, String oldValue, String converted) {
            return null;
        }

        @Override
        public int getMinApi() {
            return 0;
        }

        @Override
        public boolean requiresOldValue() {
            return false;
        }
    }

    private static class ViewStubDirectCall extends SetterCall {
        private final SetterCall mWrappedCall;

        public ViewStubDirectCall(String name, ModelClass viewType, Expr expr) {
            mWrappedCall = SetterStore.get(ModelAnalyzer.getInstance()).getSetterCall(name,
                    viewType, expr.getResolvedType(), expr.getModel().getImports());
        }

        @Override
        protected String toJavaInternal(String viewExpression, String converted) {
            return "if (!" + viewExpression + ".isInflated()) " +
                    mWrappedCall.toJava(viewExpression + ".getViewStub()", null, converted);
        }

        @Override
        protected String toJavaInternal(String viewExpression, String oldValue, String converted) {
            return null;
        }

        @Override
        public int getMinApi() {
            return 0;
        }

        @Override
        public boolean requiresOldValue() {
            return false;
        }
    }
}
