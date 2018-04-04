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

import android.databinding.testapp.databinding.InnerClassAccessBinding;
import android.databinding.testapp.vo.InnerClassOwner;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InnerClassAccessTest extends BaseDataBinderTest<InnerClassAccessBinding> {
    public InnerClassAccessTest() {
        super(InnerClassAccessBinding.class);
    }

    @Test
    @UiThreadTest
    public void testValue() {
        initBinder();
        InnerClassOwner owner = new InnerClassOwner();
        owner.nonStaticInner.value2 = "b";
        InnerClassOwner.StaticInner staticInner = new InnerClassOwner.StaticInner();
        staticInner.value1 = "a";
        mBinder.setNonStaticRef(owner.nonStaticInner);
        mBinder.setStaticRef(staticInner);
        mBinder.executePendingBindings();
        assertEquals("a", mBinder.staticView.getText().toString());
        assertEquals("b", mBinder.nonStaticView.getText().toString());
    }
}
