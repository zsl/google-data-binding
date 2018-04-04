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

import android.databinding.testapp.databinding.FindMethodTestBinding;
import android.databinding.testapp.vo.FindMethodBindingObject;
import android.support.test.annotation.UiThreadTest;
import android.widget.TextView;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FindMethodTest
        extends BindingAdapterTestBase<FindMethodTestBinding, FindMethodBindingObject> {

    public FindMethodTest() {
        super(FindMethodTestBinding.class, FindMethodBindingObject.class, R.layout.find_method_test);
    }

    @Test
    public void testNoArg() {
        TextView textView = mBinder.textView6;
        assertEquals("no arg", textView.getText().toString());
    }

    @Test
    public void testIntArg() {
        TextView textView = mBinder.textView0;
        assertEquals("1", textView.getText().toString());
    }

    @Test
    public void testFloatArg() {
        TextView textView = mBinder.textView1;
        assertEquals("1.25", textView.getText().toString());
    }

    @Test
    public void testStringArg() {
        TextView textView = mBinder.textView2;
        assertEquals("hello", textView.getText().toString());
    }

    @Test
    public void testBoxedArg() {
        TextView textView = mBinder.textView3;
        assertEquals("1", textView.getText().toString());
    }

    @Test
    public void testInheritedMethod() {
        TextView textView = mBinder.textView4;
        assertEquals("base", textView.getText().toString());
    }

    @Test
    public void testInheritedMethodInt() {
        TextView textView = mBinder.textView5;
        assertEquals("base 2", textView.getText().toString());
    }

    @Test
    public void testStaticMethod() {
        TextView textView = mBinder.textView7;
        assertEquals("world", textView.getText().toString());
    }

    @Test
    public void testStaticField() {
        TextView textView = mBinder.textView8;
        assertEquals("hello world", textView.getText().toString());
    }

    @Test
    public void testImportStaticMethod() {
        TextView textView = mBinder.textView9;
        assertEquals("world", textView.getText().toString());
    }

    @Test
    public void testImportStaticField() {
        TextView textView = mBinder.textView10;
        assertEquals("hello world", textView.getText().toString());
    }

    @Test
    public void testAliasStaticMethod() {
        TextView textView = mBinder.textView11;
        assertEquals("world", textView.getText().toString());
    }

    @Test
    public void testAliasStaticField() {
        TextView textView = mBinder.textView12;
        assertEquals("hello world", textView.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testObservableField() {
        // tests an ObservableField inside an Observable object
        assertEquals("", mBinder.textView25.getText().toString());
        mBinder.getObj().myField.set("Hello World");
        mBinder.executePendingBindings();
        assertEquals("Hello World", mBinder.textView25.getText().toString());

        mBinder.getObj().myField.set("World Hello");
        mBinder.executePendingBindings();
        assertEquals("World Hello", mBinder.textView25.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testObservableInstanceField() {
        assertEquals("", mBinder.textView26.getText().toString());
        mBinder.getObj().observableClass.setX("foobar");
        mBinder.executePendingBindings();
        assertEquals("foobar", mBinder.textView26.getText().toString());
        mBinder.getObj().observableClass.setX("barfoo");
        mBinder.executePendingBindings();
        assertEquals("barfoo", mBinder.textView26.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testPrimitiveToObject() {
        mBinder.executePendingBindings();
        assertTrue(mBinder.textView27.getTag() instanceof Integer);
        assertEquals((Integer) 1, mBinder.textView27.getTag());
    }

    @Test
    @UiThreadTest
    public void testFindMethodBasedOnSecondParam() {
        mBinder.executePendingBindings();
        assertEquals("2", mBinder.textView28.getText().toString());
        assertEquals("10", mBinder.textView29.getText().toString());
    }
}
