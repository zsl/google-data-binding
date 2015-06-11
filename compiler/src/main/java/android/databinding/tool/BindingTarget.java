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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import android.databinding.tool.expr.Expr;
import android.databinding.tool.expr.ExprModel;
import android.databinding.tool.reflection.ModelAnalyzer;
import android.databinding.tool.reflection.ModelClass;
import android.databinding.tool.store.ResourceBundle;
import android.databinding.tool.store.SetterStore;
import android.databinding.tool.util.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingTarget {
    List<Binding> mBindings = new ArrayList<Binding>();
    ExprModel mModel;
    ModelClass mResolvedClass;

    // if this target presents itself in multiple layout files with different view types,
    // it receives an interface type and should use it in the getter instead.
    private ResourceBundle.BindingTargetBundle mBundle;

    public BindingTarget(ResourceBundle.BindingTargetBundle bundle) {
        mBundle = bundle;
    }

    public boolean isUsed() {
        return mBundle.isUsed();
    }

    public void addBinding(String name, Expr expr) {
        mBindings.add(new Binding(this, name, expr));
    }

    public String getInterfaceType() {
        return mBundle.getInterfaceType() == null ? mBundle.getFullClassName() : mBundle.getInterfaceType();
    }

    public String getId() {
        return mBundle.getId();
    }

    public String getTag() {
        return mBundle.getTag();
    }

    public String getOriginalTag() {
        return mBundle.getOriginalTag();
    }

    public String getViewClass() {
        return mBundle.getFullClassName();
    }

    public ModelClass getResolvedType() {
        if (mResolvedClass == null) {
            mResolvedClass = ModelAnalyzer.getInstance().findClass(mBundle.getFullClassName(),
                    mModel.getImports());
        }
        return mResolvedClass;
    }

    public String getIncludedLayout() {
        return mBundle.getIncludedLayout();
    }

    public boolean isBinder() {
        return getIncludedLayout() != null;
    }

    public boolean isFragment() {
        return "fragment".equals(getViewClass());
    }

    public boolean supportsTag() {
        return !SetterStore.get(ModelAnalyzer.getInstance())
                .isUntaggable(mBundle.getFullClassName());
    }

    public List<Binding> getBindings() {
        return mBindings;
    }

    public ExprModel getModel() {
        return mModel;
    }

    public void setModel(ExprModel model) {
        mModel = model;
    }

    /**
     * Called after BindingTarget is finalized.
     * <p>
     * We traverse all bindings and ask SetterStore to figure out if any can be combined.
     * When N bindings are combined, they are demoted from being a binding expression and a new
     * ArgList expression is added as the new binding expression that depends on others.
     */
    public void resolveMultiSetters() {
        L.d("resolving multi setters for %s", getId());
        final SetterStore setterStore = SetterStore.get(ModelAnalyzer.getInstance());
        final String[] attributes = new String[mBindings.size()];
        final ModelClass[] types = new ModelClass[mBindings.size()];
        for (int i = 0; i < mBindings.size(); i ++) {
            Binding binding = mBindings.get(i);
            attributes[i] = binding.getName();
            types[i] = binding.getExpr().getResolvedType();
        }
        final List<SetterStore.MultiAttributeSetter> multiAttributeSetterCalls = setterStore
                .getMultiAttributeSetterCalls(attributes, getResolvedType(), types);
        if (multiAttributeSetterCalls.isEmpty()) {
            return;
        }
        final Map<String, Binding> lookup = new HashMap<String, Binding>();
        for (Binding binding : mBindings) {
            String name = binding.getName();
            if (name.startsWith("android:")) {
                lookup.put(name, binding);
            } else {
                int ind = name.indexOf(":");
                if (ind == -1) {
                    lookup.put(name, binding);
                } else {
                    lookup.put(name.substring(ind + 1), binding);
                }
            }
        }
        List<MergedBinding> mergeBindings = new ArrayList<MergedBinding>();
        for (final SetterStore.MultiAttributeSetter setter : multiAttributeSetterCalls) {
            L.d("resolved %s", setter);
            final Binding[] mergedBindings = Iterables.toArray(
                    Iterables.transform(Arrays.asList(setter.attributes),
                            new Function<String, Binding>() {
                                @Override
                                public Binding apply(final String attribute) {
                                    L.d("looking for binding for attribute %s", attribute);
                                    return lookup.get(attribute);
                                }
                            }), Binding.class) ;
            Preconditions.checkArgument(mergedBindings.length == setter.attributes.length);
            for (Binding binding : mergedBindings) {
                binding.getExpr().setBindingExpression(false);
                mBindings.remove(binding);
            }
            MergedBinding mergedBinding = new MergedBinding(getModel(), setter, this,
                    Arrays.asList(mergedBindings));
            mergeBindings.add(mergedBinding);
        }
        for (MergedBinding binding : mergeBindings) {
            mBindings.add(binding);
        }
    }
}
