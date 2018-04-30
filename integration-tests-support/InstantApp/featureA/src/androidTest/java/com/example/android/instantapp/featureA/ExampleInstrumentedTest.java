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

package com.example.android.instantapp.featureA;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.instantapp.featureA.test", appContext.getPackageName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void featureBDoesNotExist() throws ClassNotFoundException {
        Class.forName("com.example.android.instantapp.featureB.MainBActivity", false,
                InstrumentationRegistry.getTargetContext().getClassLoader());
    }

    @Test
    public void featureAExists() throws ClassNotFoundException {
        Class.forName("com.example.android.instantapp.featureA.FeatureAActivity", false,
                InstrumentationRegistry.getTargetContext().getClassLoader());
    }
}