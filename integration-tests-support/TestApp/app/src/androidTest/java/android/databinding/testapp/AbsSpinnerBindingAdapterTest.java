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

import android.databinding.testapp.databinding.AbsSpinnerAdapterTestBinding;
import android.databinding.testapp.vo.AbsSpinnerBindingObject;
import android.os.Build;
import android.support.test.annotation.UiThreadTest;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class AbsSpinnerBindingAdapterTest
        extends BindingAdapterTestBase<AbsSpinnerAdapterTestBinding, AbsSpinnerBindingObject> {

    Spinner mView;

    public AbsSpinnerBindingAdapterTest() {
        super(AbsSpinnerAdapterTestBinding.class, AbsSpinnerBindingObject.class,
                R.layout.abs_spinner_adapter_test);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mView = mBinder.view;
    }

    @Test
    @UiThreadTest
    public void testEntries() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            validateEntries();

            changeValues();

            validateEntries();
        }
    }

    @Test
    @UiThreadTest
    public void testList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            validateList();

            mBindingObject.getList().add(1, "Cruel");
            mBinder.executePendingBindings();

            validateList();
        }
    }

    @Test
    @UiThreadTest
    public void testAdapterAndSelectedItemPosition() {
        mBindingObject.setSelectedItemPosition(1);
        // Execute the pending bindings to ensure that the selected item
        // position is set prior to changing the adapter. This forces
        // a previous bug in which setting the adapter after setting
        // the selectedItemPosition caused the selected position to be
        // reset.
        mBinder.executePendingBindings();
        mBindingObject.setContext(getActivity());
        mBinder.executePendingBindings();
        assertEquals(1, mBinder.view3.getSelectedItemPosition());
        assertEquals(1, mBinder.view4.getSelectedItemPosition());
        assertSame(mBindingObject.getAdapter(), mBinder.view3.getAdapter());
        assertSame(mBindingObject.getAdapter(), mBinder.view4.getAdapter());
    }

    @Test
    @UiThreadTest
    public void testSelectedItemPosition() {
        mBindingObject.setContext(getActivity());
        mBinder.view5.setAdapter(mBindingObject.getAdapter());
        mBinder.view6.setAdapter(mBindingObject.getAdapter());
        mBindingObject.setSelectedItemPosition(1);
        mBinder.executePendingBindings();
        assertEquals(1, mBinder.view5.getSelectedItemPosition());
        assertEquals(1, mBinder.view6.getSelectedItemPosition());
    }

    private void validateEntries() {
        assertEquals(mBindingObject.getEntries().length, mView.getAdapter().getCount());
        CharSequence[] entries = mBindingObject.getEntries();
        SpinnerAdapter adapter = mView.getAdapter();
        for (int i = 0; i < entries.length; i++) {
            assertEquals(adapter.getItem(i), entries[i]);
        }
    }

    private void validateList() {
        List<String> entries = mBindingObject.getList();
        SpinnerAdapter adapter = mBinder.view2.getAdapter();
        assertEquals(entries.size(), adapter.getCount());
        for (int i = 0; i < entries.size(); i++) {
            assertEquals(adapter.getItem(i), entries.get(i));
        }
    }
}
