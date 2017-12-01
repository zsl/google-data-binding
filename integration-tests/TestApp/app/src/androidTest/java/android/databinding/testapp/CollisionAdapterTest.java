/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.databinding.testapp.databinding.CollisionBinding;
import android.test.UiThreadTest;

public class CollisionAdapterTest extends BaseDataBinderTest<CollisionBinding>{
    public CollisionAdapterTest() {
        super(CollisionBinding.class);
    }

    /**
     * Make sure the correct getter is being used in the two-way data binding.
     * We don't want it to be confused between the Double and Integer return values.
     */
    @UiThreadTest
    public void testDoubleIntConfusion() {
        initBinder();
        mBinder.setIntVal(1);
        mBinder.setDoubleVal(1.0);
        mBinder.executePendingBindings();
        assertEquals("1", mBinder.textView.getText().toString());
        assertEquals("1.0", mBinder.textView2.getText().toString());

        mBinder.textView.setText("5");
        mBinder.textView2.setText("2.0");
        assertEquals(5, mBinder.getIntVal().intValue());
        assertEquals(2.0, mBinder.getDoubleVal().doubleValue(), 0.001);
    }

    /**
     * Make sure the correct getter is being used in the two-way data binding.
     * We don't want it to be confused between the Long and long return values.
     */
    @UiThreadTest
    public void testLongConfusion() {
        initBinder();
        mBinder.setLongVal(1);
        mBinder.setLongObjVal(2L);
        mBinder.executePendingBindings();
        // the long BindingAdapter multiplies by 10
        assertEquals("10", mBinder.textView3.getText().toString());
        assertEquals("2", mBinder.textView4.getText().toString());

        mBinder.textView3.setText("50");
        mBinder.textView4.setText("2");
        assertEquals(5, mBinder.getLongVal());
        assertEquals(2, mBinder.getLongObjVal().longValue());
    }
}
