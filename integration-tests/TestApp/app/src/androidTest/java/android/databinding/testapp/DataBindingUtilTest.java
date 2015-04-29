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

package android.databinding.testapp;

import android.databinding.DataBindingUtil;
import android.databinding.testapp.databinding.BasicBindingBinding;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnPreDrawListener;

public class DataBindingUtilTest
        extends ActivityInstrumentationTestCase2<TestActivity> {

    public DataBindingUtilTest() {
        super(TestActivity.class);
    }

    @UiThreadTest
    public void testFindBinding() throws Throwable {
        BasicBindingBinding binding = BasicBindingBinding.inflate(getActivity().getLayoutInflater());
        assertEquals(binding, DataBindingUtil.findBinding(binding.textView));
        assertEquals(binding, DataBindingUtil.findBinding(binding.getRoot()));
        ViewGroup root = (ViewGroup) binding.getRoot();
        getActivity().getLayoutInflater().inflate(R.layout.basic_binding, root, true);
        View inflated = root.getChildAt(1);
        assertNull(DataBindingUtil.findBinding(inflated));
        BasicBindingBinding innerBinding = DataBindingUtil.bind(inflated);
        assertEquals(innerBinding, DataBindingUtil.findBinding(inflated));
        assertEquals(innerBinding, DataBindingUtil.findBinding(innerBinding.textView));
    }

    @UiThreadTest
    public void testGetBinding() throws Throwable {
        BasicBindingBinding binding = BasicBindingBinding.inflate(getActivity().getLayoutInflater());
        assertNull(DataBindingUtil.getBinding(binding.textView));
        assertEquals(binding, DataBindingUtil.getBinding(binding.getRoot()));
    }
}
