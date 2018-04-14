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
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VersionTest extends BaseDataBinderTest<VersionCheckBinding> {

    public VersionTest() {
        super(VersionCheckBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataBinderTrojan.setBuildSdkInt(Build.VERSION_CODES.ICE_CREAM_SANDWICH);
    }

    @After
    public void tearDown() throws Exception {
        DataBinderTrojan.setBuildSdkInt(Build.VERSION.SDK_INT);
    }

    @Test
    @UiThreadTest
    public void testCast() {
        initBinder();
        Drawable drawable = new ColorDrawable(Color.BLUE);
        mBinder.setDrawable(drawable);
        mBinder.executePendingBindings();

        assertEquals(drawable, mBinder.view.getForeground());
    }
}
