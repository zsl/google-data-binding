/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.databinding.ObservableBoolean;
import android.databinding.testapp.databinding.ObservableAdapterBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TODO: Remove this test after support for ObservableFields in BindingAdapters has been removed.
 * This should probably be in Android Studio 2.4
 */
@RunWith(AndroidJUnit4.class)
public class ObservableAdapterTest extends BaseDataBinderTest<ObservableAdapterBinding> {
    public ObservableAdapterTest() {
        super(ObservableAdapterBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initBinder(new Runnable() {
            @Override
            public void run() {
                mBinder.executePendingBindings();
            }
        });
    }

    @Test
    @UiThreadTest
    public void testSingleAdapter() {
        assertFalse(mBinder.view1.isEnabled());
        ObservableBoolean val = new ObservableBoolean(true);
        mBinder.setVal1(val);
        mBinder.executePendingBindings();
        assertTrue(mBinder.view1.isEnabled());
    }

    @Test
    @UiThreadTest
    public void testMultiAdapter() {
        assertFalse(mBinder.view2.isEnabled());
        assertFalse(mBinder.view2.isFocusable());
        ObservableBoolean val2 = new ObservableBoolean(true);
        ObservableBoolean val3 = new ObservableBoolean(true);
        mBinder.setVal2(val2);
        mBinder.setVal3(val3);
        mBinder.executePendingBindings();
        assertTrue(mBinder.view2.isEnabled());
        assertTrue(mBinder.view2.isFocusable());
    }
}
