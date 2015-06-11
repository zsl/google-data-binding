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

import android.databinding.testapp.databinding.FragmentTest1Binding;
import android.databinding.testapp.databinding.BasicBindingBinding;
import android.test.UiThreadTest;
import android.widget.TextView;

public class FragmentTest1 extends BaseDataBinderTest<FragmentTest1Binding> {

    public FragmentTest1() {
        super(FragmentTest1Binding.class);
    }

    @UiThreadTest
    public void testSimpleFragment() throws Throwable {
        FragmentTest1Binding binding = initBinder();
        assertNotNull(binding.fragment1);
        assertNotNull(binding.fragment5);
        assertNotNull(binding.fragment1Binding);
        assertNull(binding.fragment5Binding);
        binding.setA("Hello");
        binding.setB(" World");
        binding.executePendingBindings();
        BasicBindingBinding binding2 = (BasicBindingBinding) binding.fragment1Binding;
        assertEquals("Hello World", binding2.textView.getText().toString());
    }
}
