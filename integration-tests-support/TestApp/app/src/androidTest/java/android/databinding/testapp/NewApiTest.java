/*
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.databinding.testapp;

import android.databinding.DataBinderTrojan;
import android.databinding.testapp.databinding.NewApiLayoutBinding;
import android.os.Build;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public class NewApiTest extends BaseDataBinderTest<NewApiLayoutBinding> {
    public NewApiTest() {
        super(NewApiLayoutBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    @UiThreadTest
    public void testSetElevation() {
        initBinder();
        mBinder.setElevation(3);
        mBinder.setName("foo");
        mBinder.setChildren(new ArrayList<View>());
        mBinder.executePendingBindings();
        assertEquals("foo", mBinder.textView.getText().toString());
        assertEquals(3f, mBinder.textView.getElevation(), 0f);
    }

    @Test
    @UiThreadTest
    public void testSetElevationOlderAPI() {
        initBinder();
        DataBinderTrojan.setBuildSdkInt(1);
        try {
            TextView textView = mBinder.textView;
            float originalElevation = textView.getElevation();
            mBinder.setElevation(3);
            mBinder.setName("foo2");
            mBinder.executePendingBindings();
            assertEquals("foo2", textView.getText().toString());
            assertEquals(originalElevation, textView.getElevation(), 0f);
        } finally {
            DataBinderTrojan.setBuildSdkInt(Build.VERSION.SDK_INT);
        }
    }

    @Test
    @UiThreadTest
    public void testGeneric() {
        initBinder();
        ArrayList<View> views = new ArrayList<>();
        mBinder.setChildren(views);
        mBinder.executePendingBindings();
        assertEquals(1, views.size());
        assertSame(mBinder.textView, views.get(0));
    }

    @Test
    @UiThreadTest
    public void testGenericOlderApi() {
        initBinder();
        DataBinderTrojan.setBuildSdkInt(1);
        try {
            ArrayList<View> views = new ArrayList<>();
            mBinder.setChildren(views);
            mBinder.executePendingBindings();
            // we should not call the api on older platforms.
            assertEquals(0, views.size());
        } finally {
            DataBinderTrojan.setBuildSdkInt(Build.VERSION.SDK_INT);
        }
    }
}
