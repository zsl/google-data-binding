/*
 * Copyright (C) 2015 The Android Open Source Project
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

import android.databinding.testapp.databinding.GenericAdapterBinding;
import android.databinding.testapp.vo.BasicObject;
import android.databinding.testapp.vo.GenericContainer;
import android.test.UiThreadTest;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GenericAdapterTest extends BaseDataBinderTest<GenericAdapterBinding> {

    public GenericAdapterTest() {
        super(GenericAdapterBinding.class);
    }

    @UiThreadTest
    @Test
    public void testGenericArgs() throws Throwable {
        initBinder();

        String[] arr = { "Hello", "World" };
        List<String> list = Arrays.asList(arr);
        BasicObject obj = new BasicObject();
        GenericContainer<BasicObject> container = new GenericContainer<>(obj);
        obj.setField1("Yes, it worked");
        getBinder().setList(list);
        getBinder().setArr(arr);
        getBinder().setContainer(container);
        getBinder().executePendingBindings();
        assertEquals("Hello World", getBinder().textView1.getText().toString());
        assertEquals("Hello World", getBinder().textView2.getText().toString());
        assertEquals("Hello World", getBinder().textView3.getText().toString());
        assertEquals("Hello World", getBinder().textView4.getText().toString());
        assertEquals(list, getBinder().view5.getList());
        assertEquals(list, getBinder().view6.getList());
        assertEquals("Hello World", getBinder().textView7.getText().toString());
        assertEquals("Yes, it worked", getBinder().textView8.getText().toString());
        assertEquals("Yes, it worked", getBinder().textView9.getText().toString());
    }
}
