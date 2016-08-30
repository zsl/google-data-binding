/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.databinding.ViewDataBinding;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.view.LayoutInflater;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestActivityTestBase<T extends ViewDataBinding, U extends TestActivity>
        extends ActivityInstrumentationTestCase2<U> {
    protected Class<T> mBinderClass;
    protected T mBinder;

    public TestActivityTestBase(final Class<T> binderClass,
            final Class<U> activityClass) {
        super(activityClass);
        mBinderClass = binderClass;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    protected T getBinder() {
        return mBinder;
    }

    protected T initBinder() {
        return initBinder(null);
    }

    @Override
    public void runTestOnUiThread(Runnable r) throws Throwable {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            // ensure activity is created
            getActivity();
            super.runTestOnUiThread(r);
        }

    }

    protected T initBinder(final Runnable init) {
        assertNull("should not initialize binder twice", mBinder);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            getActivity();// ensure activity is created
            getInstrumentation().waitForIdleSync();
        }

        final Method[] method = {null};
        Throwable[] initError = new Throwable[1];
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        method[0] = mBinderClass.getMethod("inflate", LayoutInflater.class);
                        mBinder = (T) method[0].invoke(null, getActivity().getLayoutInflater());
                        getActivity().setContentView(mBinder.getRoot());
                        if (init != null) {
                            init.run();
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        fail("Error creating binder: " + sw.toString());
                    }
                }
            });
        } catch (Throwable throwable) {
            initError[0] = throwable;
        }
        assertNull(initError[0]);
        assertNotNull(mBinder);
        return mBinder;
    }

    protected void reCreateBinder(Runnable init) {
        mBinder = null;
        initBinder(init);
    }

    protected void assertMethod(Class<?> klass, String methodName) throws NoSuchMethodException {
        assertEquals(klass, getDeclaredMethodWithInherited(methodName, mBinder.getClass()).getReturnType());
    }

    protected void assertField(Class<?> klass, String fieldName) throws NoSuchFieldException {
        assertEquals(klass, getDeclaredFieldWithInherited(fieldName, mBinder.getClass()).getType());
    }

    protected static Field getDeclaredFieldWithInherited(String fieldName, Class klass) {
        if (klass == null || klass == Object.class) {
            return null;
        }
        try {
            return klass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return getDeclaredFieldWithInherited(fieldName, klass.getSuperclass());
        }
    }

    protected static Method getDeclaredMethodWithInherited(String methodName, Class klass) {
        if (klass == null || klass == Object.class) {
            return null;
        }
        try {
            return klass.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return getDeclaredMethodWithInherited(methodName, klass.getSuperclass());
        }
    }

    protected void assertPublicField(Class<?> klass, String fieldName) throws NoSuchFieldException {
        assertEquals(klass, mBinder.getClass().getField(fieldName).getType());
    }

    protected void assertNoField(String fieldName) {
        Exception[] ex = new Exception[1];
        try {
            mBinder.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            ex[0] = e;
        }
        assertNotNull(ex[0]);
    }
}
