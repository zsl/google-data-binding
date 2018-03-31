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


import android.databinding.testapp.databinding.StaticAccessImportOnDemandBinding;
import android.databinding.testapp.vo.StaticTestsVo;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StaticAccessImportOnDemandTest extends BaseDataBinderTest<StaticAccessImportOnDemandBinding> {

    public StaticAccessImportOnDemandTest() {
        super(StaticAccessImportOnDemandBinding.class);
    }

    @Test
    @UiThreadTest
    public void testAccessStatics() {
        initBinder();
        StaticTestsVo vo = new StaticTestsVo();
        mBinder.setVo(vo);
        assertStaticContents();
    }

    private void assertStaticContents() {
        mBinder.executePendingBindings();
        assertText(StaticTestsVo.ourStaticField, mBinder.staticFieldOverVo);
        assertText(StaticTestsVo.ourStaticMethod(), mBinder.staticMethodOverVo);
        assertText(StaticTestsVo.ourStaticObservable.get(), mBinder.obsStaticOverVo);

        String newValue = UUID.randomUUID().toString();
        StaticTestsVo.ourStaticObservable.set(newValue);
        mBinder.executePendingBindings();
        assertText(StaticTestsVo.ourStaticObservable.get(), mBinder.obsStaticOverVo);
    }

    @Test
    @UiThreadTest
    public void testAccessStaticsVoInstance() {
        initBinder();
        mBinder.setVo(null);
        assertStaticContents();
    }

    private void assertText(String contents, TextView textView) {
        assertEquals(contents, textView.getText().toString());
    }
}
