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
package android.databinding.testapp;

import android.content.pm.ActivityInfo;
import android.databinding.testapp.BaseDataBinderTest;
import android.databinding.testapp.R;
import android.databinding.testapp.databinding.FragmentTest2Binding;
import android.databinding.testapp.databinding.BasicBindingBinding;
import android.test.UiThreadTest;
import android.widget.TextView;

public class FragmentTest2 extends BaseDataBinderTest<FragmentTest2Binding> {

    public FragmentTest2() {
        super(FragmentTest2Binding.class, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @UiThreadTest
    public void testMultiLayoutFragment() throws Throwable {
        FragmentTest2Binding binding = initBinder();
        assertNotNull(binding.fragment1);
        assertNotNull(binding.fragment2);
        assertNull(binding.fragment4);
        assertNotNull(binding.fragment1Binding);
        assertNotNull(binding.fragment2Binding);
        assertNull(binding.fragment4Binding);
        assertNotNull(getActivity().getFragmentManager().findFragmentByTag("hello"));
        assertNotNull(getActivity().getFragmentManager().findFragmentByTag("world"));
        binding.setA("Hello");
        binding.setB(" World");
        binding.executePendingBindings();
        BasicBindingBinding binding1 = (BasicBindingBinding) binding.fragment1Binding;
        BasicBindingBinding binding2 = (BasicBindingBinding) binding.fragment2Binding;
        assertEquals("Hello World", binding1.textView.getText().toString());
        assertEquals("Hello World", binding2.textView.getText().toString());
    }
}
