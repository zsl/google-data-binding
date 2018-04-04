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

import android.databinding.ViewStubProxy;
import android.databinding.testapp.databinding.ListenersBinding;
import android.databinding.testapp.vo.ListenerBindingObject;
import android.databinding.testapp.vo.ListenerBindingObject.Inner;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ListenerTest extends BaseDataBinderTest<ListenersBinding> {
    private ListenerBindingObject mBindingObject;

    public ListenerTest() {
        super(ListenersBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        mBindingObject = new ListenerBindingObject(getActivity());
        super.setUp();
        initBinder(new Runnable() {
            @Override
            public void run() {
                mBinder.setObj(mBindingObject);
            }
        });
        ListenerBindingObject.lastClick = 0;
    }

    @Test
    @UiThreadTest
    public void testInstanceClick() {
        View view = mBinder.click1;
        assertEquals(0, ListenerBindingObject.lastClick);
        view.callOnClick();
        assertEquals(1, ListenerBindingObject.lastClick);
    }

    @Test
    @UiThreadTest
    public void testStaticClick() {
        View view = mBinder.click2;
        assertEquals(0, ListenerBindingObject.lastClick);
        view.callOnClick();
        assertEquals(2, ListenerBindingObject.lastClick);
    }

    @Test
    @UiThreadTest
    public void testInstanceClickTwoArgs() {
        View view = mBinder.click3;
        assertEquals(0, ListenerBindingObject.lastClick);
        view.callOnClick();
        assertEquals(3, ListenerBindingObject.lastClick);
        assertTrue(view.isClickable());
        ListenerBindingObject.lastClick = 0;
        mBindingObject.clickable.set(false);
        mBinder.executePendingBindings();
        assertFalse(view.isClickable());
        mBindingObject.useOne.set(true);
        mBinder.executePendingBindings();
        assertFalse(view.isClickable());
        mBindingObject.clickable.set(true);
        mBinder.executePendingBindings();
        view.callOnClick();
        assertEquals(1, ListenerBindingObject.lastClick);
    }

    @Test
    @UiThreadTest
    public void testStaticClickTwoArgs() {
        View view = mBinder.click4;
        assertEquals(0, ListenerBindingObject.lastClick);
        view.callOnClick();
        assertEquals(4, ListenerBindingObject.lastClick);
        assertTrue(view.isClickable());
        ListenerBindingObject.lastClick = 0;
        mBindingObject.clickable.set(false);
        mBinder.executePendingBindings();
        assertFalse(view.isClickable());
        view.setClickable(true);
        view.callOnClick();
        assertEquals(4, ListenerBindingObject.lastClick);
    }

    @Test
    @UiThreadTest
    public void testClickExpression() {
        View view = mBinder.click5;
        assertEquals(0, ListenerBindingObject.lastClick);
        view.callOnClick();
        assertEquals(2, ListenerBindingObject.lastClick);
        ListenerBindingObject.lastClick = 0;
        mBindingObject.useOne.set(true);
        mBinder.executePendingBindings();
        view.callOnClick();
        assertEquals(1, ListenerBindingObject.lastClick);
    }

    @Test
    @UiThreadTest
    public void testInflateListener() {
        ViewStubProxy viewStubProxy = mBinder.viewStub;
        assertFalse(viewStubProxy.isInflated());
        assertFalse(mBindingObject.inflateCalled);
        viewStubProxy.getViewStub().inflate();
        assertTrue(mBindingObject.inflateCalled);
        assertTrue(viewStubProxy.isInflated());
    }

    @Test
    @UiThreadTest
    public void testBaseObservableClick() {
        View view = mBinder.click6;
        Inner inner = new Inner();
        mBinder.setObj2(inner);
        mBinder.executePendingBindings();
        assertFalse(inner.clicked);
        view.callOnClick();
        assertTrue(inner.clicked);
    }
}
