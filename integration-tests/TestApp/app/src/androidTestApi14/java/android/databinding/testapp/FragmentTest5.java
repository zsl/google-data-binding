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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.testapp.databinding.FragmentTest5Binding;
import android.databinding.testapp.databinding.FragmentTest6Binding;
import android.test.UiThreadTest;
import android.widget.TextView;

public class FragmentTest5 extends BaseDataBinderTest<FragmentTest6Binding> {

    public FragmentTest5() {
        super(FragmentTest6Binding.class);
    }

    @UiThreadTest
    public void testFragmentConflicts() throws Throwable {
        FragmentTest6Binding binding = initBinder();
        FragmentTest5Binding binding5 = binding.fragment;
        assertNotNull(binding5);
        assertNotNull(binding5.fragment3);
        assertNotNull(binding5.fragment4);
        assertNotNull(binding5.fragment5);
        assertNotNull(binding5.fragment3Binding);
        assertNotNull(binding5.fragment4Binding);
        assertNull(binding5.fragment5Binding);
        binding.setA("Hello");
        binding.setB(" World");
        binding.executePendingBindings();
        TextView textView = (TextView) binding.fragment.fragment3.findViewById(R.id.textView);
        assertEquals("Hello World", textView.getText().toString());
    }
}