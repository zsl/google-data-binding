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
import com.google.common.collect.Iterables;

import org.apache.commons.lang3.StringUtils;

import android.databinding.tool.expr.ArgListExpr;
import android.databinding.tool.expr.Expr;
import android.databinding.tool.expr.ExprModel;
import android.databinding.tool.reflection.ModelAnalyzer;
import android.databinding.tool.store.SetterStore;
import android.databinding.tool.util.L;
import android.databinding.tool.writer.CodeGenUtil;

import java.util.List;

/**
 * Multiple binding expressions can be evaluated using a single adapter. In those cases,
 * we replace the Binding with a MergedBinding.
 */
public class MergedBinding extends Binding {
    private final SetterStore.MultiAttributeSetter mMultiAttributeSetter;
    public MergedBinding(ExprModel model, SetterStore.MultiAttributeSetter multiAttributeSetter,
            BindingTarget target, Iterable<Binding> bindings) {
        super(target, createMergedName(bindings), createArgListExpr(model, bindings));
        mMultiAttributeSetter = multiAttributeSetter;
    }

    private static Expr createArgListExpr(ExprModel model, final Iterable<Binding> bindings) {
        Expr expr = model.argListExpr(Iterables.transform(bindings, new Function<Binding, Expr>() {
            @Override
            public Expr apply(Binding input) {
                return input.getExpr();
            }
        }));
        expr.setBindingExpression(true);
        return expr;
    }

    private static String createMergedName(Iterable<Binding> bindings) {
        return Iterables.toString(Iterables.transform(bindings, new Function<Binding, String>() {
            @Override
            public String apply(Binding input) {
                return input.getName();
            }
        }));
    }

    @Override
    public int getMinApi() {
        return 1;
    }

    @Override
    public String toJavaCode(String targetViewName) {
        ArgListExpr args = (ArgListExpr) getExpr();
        final String[] expressions = Iterables
                .toArray(Iterables.transform(args.getChildren(), new Function<Expr, String>() {
                    @Override
                    public String apply(Expr input) {
                        return CodeGenUtil.Companion.toCode(input, false).generate();
                    }
                }), String.class);
        L.d("merged binding arg: %s", args.getUniqueKey());
        return mMultiAttributeSetter.toJava(targetViewName, expressions);
    }
}
