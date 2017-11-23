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

import android.databinding.testapp.databinding.ReliesOnBinding;
import android.databinding.testapp.BR;
import android.databinding.testapp.vo.BasicObject;
import android.test.UiThreadTest;

public class BindingReliesOnTest extends BaseDataBinderTest<ReliesOnBinding> {


    public BindingReliesOnTest() {
        super(ReliesOnBinding.class);
    }

    protected void setUp() throws Exception {
        initBinder(null);
    }

    @UiThreadTest
    public void testReliesOn() {
        BasicObject obj = new BasicObject();
        obj.setField1("Hello");
        obj.setField2("World");
        obj.field3 = "1";
        mBinder.setObj(obj);
        mBinder.executePendingBindings();

        assertEquals("hello", mBinder.recurseDep.getText().toString());
        assertEquals("world", mBinder.recurseDep2.getText().toString());
        assertEquals("Hello", mBinder.field1.getText().toString());
        assertEquals("World", mBinder.field2.getText().toString());
        assertEquals("Hello World 1", mBinder.combo.getText().toString());
        assertEquals("+1", mBinder.field4.getText().toString());
        assertEquals("Hello World 1 boo", mBinder.combo2.getText().toString());

        obj.setField1("Goodbye");
        mBinder.executePendingBindings();

        assertEquals("Goodbye", mBinder.field1.getText().toString());
        assertEquals("World", mBinder.field2.getText().toString());
        assertEquals("Goodbye World 1", mBinder.combo.getText().toString());
        assertEquals("+1", mBinder.field4.getText().toString());
        assertEquals("Goodbye World 1 boo", mBinder.combo2.getText().toString());

        obj.setField2("Cruel");
        mBinder.executePendingBindings();

        assertEquals("Goodbye", mBinder.field1.getText().toString());
        assertEquals("Cruel", mBinder.field2.getText().toString());
        assertEquals("Goodbye Cruel 1", mBinder.combo.getText().toString());
        assertEquals("+1", mBinder.field4.getText().toString());
        assertEquals("Goodbye Cruel 1 boo", mBinder.combo2.getText().toString());

        obj.field3 = "World";
        obj.notifyPropertyChanged(BR.field3);
        mBinder.executePendingBindings();

        assertEquals("Goodbye", mBinder.field1.getText().toString());
        assertEquals("Cruel", mBinder.field2.getText().toString());
        assertEquals("Goodbye Cruel World", mBinder.combo.getText().toString());
        assertEquals("+World", mBinder.field4.getText().toString());
        assertEquals("Goodbye Cruel World boo", mBinder.combo2.getText().toString());

        obj.setJoin("-");
        mBinder.executePendingBindings();

        assertEquals("Goodbye", mBinder.field1.getText().toString());
        assertEquals("Cruel", mBinder.field2.getText().toString());
        assertEquals("Goodbye-Cruel-World", mBinder.combo.getText().toString());
        assertEquals("+World", mBinder.field4.getText().toString());
        assertEquals("Goodbye-Cruel-World-boo", mBinder.combo2.getText().toString());

        obj.setField5("zap");
        mBinder.executePendingBindings();

        assertEquals("Goodbye-Cruel-World-zap", mBinder.combo2.getText().toString());

        assertEquals("0", mBinder.stringVal.getText().toString());
        obj.val.set(1);
        mBinder.executePendingBindings();
        assertEquals("1", mBinder.stringVal.getText().toString());
    }
}
