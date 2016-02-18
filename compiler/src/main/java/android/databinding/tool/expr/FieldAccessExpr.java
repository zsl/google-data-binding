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

package android.databinding.tool.expr;

import android.databinding.tool.Binding;
import android.databinding.tool.BindingTarget;
import android.databinding.tool.InverseBinding;
import android.databinding.tool.ext.ExtKt;
import android.databinding.tool.processing.ErrorMessages;
import android.databinding.tool.processing.Scope;
import android.databinding.tool.reflection.Callable;
import android.databinding.tool.reflection.Callable.Type;
import android.databinding.tool.reflection.ModelAnalyzer;
import android.databinding.tool.reflection.ModelClass;
import android.databinding.tool.reflection.ModelMethod;
import android.databinding.tool.solver.ExecutionPath;
import android.databinding.tool.store.SetterStore;
import android.databinding.tool.store.SetterStore.BindingGetterCall;
import android.databinding.tool.util.BrNameUtil;
import android.databinding.tool.util.L;
import android.databinding.tool.util.Preconditions;
import android.databinding.tool.writer.KCode;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class FieldAccessExpr extends Expr {
    String mName;
    // notification name for the field. Important when we map this to a method w/ different name
    String mBrName;
    Callable mGetter;
    boolean mIsListener;
    boolean mIsViewAttributeAccess;

    FieldAccessExpr(Expr parent, String name) {
        super(parent);
        mName = name;
    }

    public Expr getTarget() {
        return getChildren().get(0);
    }

    public Callable getGetter() {
        if (mGetter == null) {
            getResolvedType();
        }
        return mGetter;
    }

    @Override
    public List<ExecutionPath> toExecutionPath(List<ExecutionPath> paths) {
        final List<ExecutionPath> targetPaths = getTarget().toExecutionPath(paths);
        // after this, we need a null check.
        List<ExecutionPath> result = new ArrayList<ExecutionPath>();
        if (getTarget() instanceof StaticIdentifierExpr) {
            result.addAll(toExecutionPathInOrder(paths, getTarget()));
        } else {
            for (ExecutionPath path : targetPaths) {
                final ComparisonExpr cmp = getModel()
                        .comparison("!=", getTarget(), getModel().symbol("null", Object.class));
                path.addPath(cmp);
                final ExecutionPath subPath = path.addBranch(cmp, true);
                if (subPath != null) {
                    subPath.addPath(this);
                    result.add(subPath);
                }
            }
        }
        return result;
    }

    @Override
    public String getInvertibleError() {
        if (getGetter().setterName == null) {
            return "Two-way binding cannot resolve a setter for " + getResolvedType().toJavaCode() +
                    " property '" + mName + "'";
        }
        if (!mGetter.isDynamic()) {
            return "Cannot change a final field in " + getResolvedType().toJavaCode() +
                    " property " + mName;
        }
        return null;
    }

    public int getMinApi() {
        return mGetter.getMinApi();
    }

    @Override
    public boolean isDynamic() {
        if (mGetter == null) {
            getResolvedType();
        }
        if (mGetter == null || mGetter.type == Type.METHOD) {
            return true;
        }
        // if it is static final, gone
        if (getTarget().isDynamic()) {
            // if owner is dynamic, then we can be dynamic unless we are static final
            return !mGetter.isStatic() || mGetter.isDynamic();
        }

        if (mIsViewAttributeAccess) {
            return true; // must be able to invalidate this
        }

        // if owner is NOT dynamic, we can be dynamic if an only if getter is dynamic
        return mGetter.isDynamic();
    }

    public boolean hasBindableAnnotations() {
        return mGetter.canBeInvalidated();
    }

    @Override
    public Expr resolveListeners(ModelClass listener, Expr parent) {
        final ModelClass childType = getTarget().getResolvedType();
        if (getGetter() == null) {
            if (listener == null || !mIsListener) {
                L.e("Could not resolve %s.%s as an accessor or listener on the attribute.",
                        childType.getCanonicalName(), mName);
                return this;
            }
            getTarget().getParents().remove(this);
        } else if (listener == null) {
            return this; // Not a listener, but we have a getter.
        }
        List<ModelMethod> abstractMethods = listener.getAbstractMethods();
        int numberOfAbstractMethods = abstractMethods == null ? 0 : abstractMethods.size();
        if (numberOfAbstractMethods != 1) {
            if (mGetter == null) {
                L.e("Could not find accessor %s.%s and %s has %d abstract methods, so is" +
                                " not resolved as a listener",
                        childType.getCanonicalName(), mName,
                        listener.getCanonicalName(), numberOfAbstractMethods);
            }
            return this;
        }

        // Look for a signature matching the abstract method
        final ModelMethod listenerMethod = abstractMethods.get(0);
        final ModelClass[] listenerParameters = listenerMethod.getParameterTypes();
        boolean isStatic = getTarget() instanceof StaticIdentifierExpr;
        List<ModelMethod> methods = childType.findMethods(mName, isStatic);
        for (ModelMethod method : methods) {
            if (acceptsParameters(method, listenerParameters) &&
                    method.getReturnType(null).equals(listenerMethod.getReturnType(null))) {
                resetResolvedType();
                // replace this with ListenerExpr in parent
                Expr listenerExpr = getModel().listenerExpr(getTarget(), mName, listener,
                        listenerMethod);
                if (parent != null) {
                    int index;
                    while ((index = parent.getChildren().indexOf(this)) != -1) {
                        parent.getChildren().set(index, listenerExpr);
                    }
                }
                if (getModel().mBindingExpressions.contains(this)) {
                    getModel().bindingExpr(listenerExpr);
                }
                getParents().remove(parent);
                if (getParents().isEmpty()) {
                    getModel().removeExpr(this);
                }
                return listenerExpr;
            }
        }

        if (mGetter == null) {
            L.e("Listener class %s with method %s did not match signature of any method %s.%s",
                    listener.getCanonicalName(), listenerMethod.getName(),
                    childType.getCanonicalName(), mName);
        }
        return this;
    }

    private boolean acceptsParameters(ModelMethod method, ModelClass[] listenerParameters) {
        ModelClass[] parameters = method.getParameterTypes();
        if (parameters.length != listenerParameters.length) {
            return false;
        }
        for (int i = 0; i < parameters.length; i++) {
            if (!parameters[i].isAssignableFrom(listenerParameters[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected List<Dependency> constructDependencies() {
        final List<Dependency> dependencies = constructDynamicChildrenDependencies();
        for (Dependency dependency : dependencies) {
            if (dependency.getOther() == getTarget()) {
                dependency.setMandatory(true);
            }
        }
        return dependencies;
    }

    @Override
    protected String computeUniqueKey() {
        return join(mName, ".", super.computeUniqueKey());
    }

    public String getName() {
        return mName;
    }

    public String getBrName() {
        if (mIsListener) {
            return null;
        }
        try {
            Scope.enter(this);
            Preconditions.checkNotNull(mGetter, "cannot get br name before resolving the getter");
            return mBrName;
        } finally {
            Scope.exit();
        }
    }

    @Override
    public void updateExpr(ModelAnalyzer modelAnalyzer) {
        try {
            Scope.enter(this);
            resolveType(modelAnalyzer);
            super.updateExpr(modelAnalyzer);
        } finally {
            Scope.exit();
        }
    }

    @Override
    protected ModelClass resolveType(ModelAnalyzer modelAnalyzer) {
        if (mIsListener) {
            return modelAnalyzer.findClass(Object.class);
        }
        if (mGetter == null) {
            Expr target = getTarget();
            target.getResolvedType();
            boolean isStatic = target instanceof StaticIdentifierExpr;
            ModelClass resolvedType = target.getResolvedType();
            L.d("resolving %s. Resolved class type: %s", this, resolvedType);

            mGetter = resolvedType.findGetterOrField(mName, isStatic);

            if (mGetter == null) {
                mIsListener = !resolvedType.findMethods(mName, isStatic).isEmpty();
                if (!mIsListener) {
                    L.e("Could not find accessor %s.%s", resolvedType.getCanonicalName(), mName);
                }
                return modelAnalyzer.findClass(Object.class);
            }

            if (mGetter.isStatic() && !isStatic) {
                // found a static method on an instance. register a new one
                replaceStaticIdentifier(resolvedType);
                target = getTarget();
            }

            if (mGetter.resolvedType.isObservableField()) {
                // Make this the ".get()" and add an extra field access for the observable field
                target.getParents().remove(this);
                getChildren().remove(target);

                FieldAccessExpr observableField = getModel().observableField(target, mName);
                getChildren().add(observableField);
                observableField.getParents().add(this);
                mGetter = mGetter.resolvedType.findGetterOrField("", false);
                mName = "";
                mBrName = ExtKt.br(mName);
            } else if (hasBindableAnnotations()) {
                mBrName = ExtKt.br(BrNameUtil.brKey(mGetter));
            }
        }
        return mGetter.resolvedType;
    }

    protected void replaceStaticIdentifier(ModelClass staticIdentifierType) {
        getTarget().getParents().remove(this);
        getChildren().remove(getTarget());
        StaticIdentifierExpr staticId = getModel().staticIdentifierFor(staticIdentifierType);
        getChildren().add(staticId);
        staticId.getParents().add(this);
    }

    @Override
    public Expr resolveTwoWayExpressions(Expr parent) {
        final Expr child = getTarget();
        if (!(child instanceof ViewFieldExpr)) {
            return this;
        }
        final ViewFieldExpr expr = (ViewFieldExpr) child;
        final BindingTarget bindingTarget = expr.getBindingTarget();

        // This is a binding to a View's attribute, so look for matching attribute
        // on that View's BindingTarget. If there is an expression, we simply replace
        // the binding with that binding expression.
        for (Binding binding : bindingTarget.getBindings()) {
            if (attributeMatchesName(binding.getName(), mName)) {
                final Expr replacement = binding.getExpr();
                replaceExpression(parent, replacement);
                return replacement;
            }
        }

        // There was no binding expression to bind to. This should be a two-way binding.
        // This is a synthesized two-way binding because we must capture the events from
        // the View and change the value when the target View's attribute changes.
        final SetterStore setterStore = SetterStore.get(ModelAnalyzer.getInstance());
        final ModelClass targetClass = expr.getResolvedType();
        BindingGetterCall getter = setterStore.getGetterCall(mName, targetClass, null, null);
        if (getter == null) {
            getter = setterStore.getGetterCall("android:" + mName, targetClass, null, null);
            if (getter == null) {
                L.e("Could not resolve the two-way binding attribute '%s' on type '%s'",
                        mName, targetClass);
            }
        }
        InverseBinding inverseBinding = null;
        for (Binding binding : bindingTarget.getBindings()) {
            final Expr testExpr = binding.getExpr();
            if (testExpr instanceof TwoWayListenerExpr &&
                    getter.getEventAttribute().equals(binding.getName())) {
                inverseBinding = ((TwoWayListenerExpr) testExpr).mInverseBinding;
                break;
            }
        }
        if (inverseBinding == null) {
            inverseBinding = bindingTarget.addInverseBinding(mName, getter);
        }
        inverseBinding.addChainedExpression(this);
        mIsViewAttributeAccess = true;
        enableDirectInvalidation();
        return this;
    }

    private static boolean attributeMatchesName(String attribute, String field) {
        int colonIndex = attribute.indexOf(':');
        return attribute.substring(colonIndex + 1).equals(field);
    }

    private void replaceExpression(Expr parent, Expr replacement) {
        if (parent != null) {
            List<Expr> children = parent.getChildren();
            int index;
            while ((index = children.indexOf(this)) >= 0) {
                children.set(index, replacement);
                replacement.getParents().add(parent);
            }
            while (getParents().remove(parent)) {
                // just remove all copies of parent.
            }
        }
        if (getParents().isEmpty()) {
            getModel().removeExpr(this);
        }
    }

    @Override
    protected String asPackage() {
        String parentPackage = getTarget().asPackage();
        return parentPackage == null ? null : parentPackage + "." + mName;
    }

    @Override
    protected KCode generateCode() {
        // once we can deprecate using Field.access for callbacks, we can get rid of this since
        // it will be detected when resolve type is run.
        Preconditions.checkNotNull(getGetter(), ErrorMessages.CANNOT_RESOLVE_TYPE, this);
        KCode code = new KCode()
                .app("", getTarget().toCode()).app(".");
        if (getGetter().type == Callable.Type.FIELD) {
            return code.app(getGetter().name);
        } else {
            return code.app(getGetter().name).app("()");
        }
    }

    @Override
    public Expr generateInverse(ExprModel model, Expr value, String bindingClassName) {
        Expr castExpr = model.castExpr(getResolvedType().toJavaCode(), value);
        Expr target = getTarget().cloneToModel(model);
        Expr result;
        if (getGetter().type == Callable.Type.FIELD) {
            result = model.assignment(target, mName, castExpr);
        } else {
            result = model.methodCall(target, mGetter.setterName, Lists.newArrayList(castExpr));
        }
        return result;
    }

    @Override
    public Expr cloneToModel(ExprModel model) {
        final Expr clonedTarget = getTarget().cloneToModel(model);
        return model.field(clonedTarget, mName);
    }

    @Override
    public String toString() {
        String name = mName.isEmpty() ? "get()" : mName;
        return getTarget().toString() + '.' + name;
    }
}
