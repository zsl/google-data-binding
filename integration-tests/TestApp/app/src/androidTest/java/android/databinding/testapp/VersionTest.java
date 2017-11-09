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

import android.databinding.DataBinderTrojan;
import android.databinding.testapp.databinding.VersionCheckBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.test.UiThreadTest;

public class VersionTest extends BaseDataBinderTest<VersionCheckBinding> {

    public VersionTest() {
        super(VersionCheckBinding.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DataBinderTrojan.setBuildSdkInt(Build.VERSION_CODES.ICE_CREAM_SANDWICH);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        DataBinderTrojan.setBuildSdkInt(Build.VERSION.SDK_INT);
    }

    @UiThreadTest
    public void testCast() throws Throwable {
        initBinder();
        Drawable drawable = new ColorDrawable(Color.BLUE);
        mBinder.setDrawable(drawable);
        mBinder.executePendingBindings();

        assertEquals(drawable, mBinder.view.getForeground());
    }
}
