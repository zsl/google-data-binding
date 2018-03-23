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

import android.databinding.testapp.databinding.CustomBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CustomBindingTest extends BaseDataBinderTest<CustomBinding> {

    public CustomBindingTest() {
        super(CustomBinding.class);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    @UiThreadTest
    public void testCustomBindings() {
        initBinder();
        mBinder.executePendingBindings();
        assertEquals("hello world", mBinder.textView.getText().toString());

        android.databinding.testapp.mypackage.CustomBinding subPackaged =
                android.databinding.testapp.mypackage.CustomBinding.inflate(
                        getActivity().getLayoutInflater());
        subPackaged.executePendingBindings();
        assertEquals("goodbye world", subPackaged.textView.getText().toString());


        com.android.test.CustomBinding newPackage =
                com.android.test.CustomBinding.inflate(getActivity().getLayoutInflater());
        newPackage.executePendingBindings();
        assertEquals("hello android", newPackage.textView.getText().toString());
    }
}
