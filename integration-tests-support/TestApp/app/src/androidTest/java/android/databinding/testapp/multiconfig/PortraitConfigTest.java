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

package android.databinding.testapp.multiconfig;

import android.databinding.testapp.BR;
import android.databinding.testapp.BaseDataBinderTest;
import android.databinding.testapp.databinding.MultiResLayoutBinding;
import android.databinding.testapp.vo.NotBindableVo;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PortraitConfigTest extends BaseDataBinderTest<MultiResLayoutBinding> {
    public PortraitConfigTest() {
        super(MultiResLayoutBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initBinder();
        waitForUISync();
    }

    @Test
    @UiThreadTest
    public void testSetVariable() {
        assertTrue(mBinder.setVariable(BR.objectInBoth, null));
        assertTrue(mBinder.setVariable(BR.objectInDefault, null));
        assertTrue(mBinder.setVariable(BR.objectInLand, null));
        assertFalse(mBinder.setVariable(BR.obj, null));
        NotBindableVo landscape = new NotBindableVo();
        mBinder.setVariable(BR.objectInLand, landscape);
        assertSame(landscape, mBinder.getObjectInLand());
    }
}
