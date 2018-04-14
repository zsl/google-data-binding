/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.example.android.appwithdatabindingintests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.databinding.DataBindingUtil;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;

import com.example.android.appwithdatabindingintests.databinding.ActivityMainBinding;
import com.example.android.appwithdatabindingintests.test.databinding.TestLayoutBinding;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DataBoundTestResourcesTest {
    @Test
    public void useTestResource() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                TestLayoutBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(InstrumentationRegistry.getContext()),
                        com.example.android.appwithdatabindingintests.test.R.layout.test_layout,
                        null, false);
                binding.setTestVar("tada");
                binding.executePendingBindings();
                assertThat(binding.text.getText().toString(), is("tada"));
            }
        });
    }

    @Test
    public void useAppResource() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ActivityMainBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(InstrumentationRegistry.getTargetContext()),
                        R.layout.activity_main, null, false);
                binding.setAppTestVar("tada");
                binding.executePendingBindings();
                assertThat(binding.myTextView.getText().toString(), is("tada"));
            }
        });

    }
}
