/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.databinding.testapp;


import android.databinding.testapp.databinding.LayoutWithObjectBooleanPredicateBinding;
import android.databinding.testapp.vo.NotBindableVo;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

@MediumTest
public class BindConditionalToBooleanObjectTest
        extends BaseDataBinderTest<LayoutWithObjectBooleanPredicateBinding>{

    public BindConditionalToBooleanObjectTest() {
        super(LayoutWithObjectBooleanPredicateBinding.class);
    }

    @UiThreadTest
    public void testNull() {
        initBinder();
        NotBindableVo vo = new NotBindableVo();
        vo.boolObject = null;
        getBinder().setVo(vo);
        mBinder.executePendingBindings();
        assertEquals(View.GONE, mBinder.getRoot().getVisibility());
    }

    @UiThreadTest
    public void testTrue() {
        initBinder();
        NotBindableVo vo = new NotBindableVo();
        vo.boolObject = true;
        getBinder().setVo(vo);
        mBinder.executePendingBindings();
        assertEquals(View.INVISIBLE, mBinder.getRoot().getVisibility());
    }

    @UiThreadTest
    public void testFalse() {
        initBinder();
        NotBindableVo vo = new NotBindableVo();
        vo.boolObject = false;
        getBinder().setVo(vo);
        mBinder.executePendingBindings();
        assertEquals(View.GONE, mBinder.getRoot().getVisibility());
    }
}
