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

import android.databinding.ListChangeRegistry;
import android.databinding.ObservableList;
import android.databinding.ObservableList.OnListChangedCallback;
import android.databinding.testapp.databinding.BasicBindingBinding;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ListChangeRegistryTest extends BaseDataBinderTest<BasicBindingBinding> {

    private ListChangeRegistry mListChangeRegistry;

    private int mCallCount;

    public ListChangeRegistryTest() {
        super(BasicBindingBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mListChangeRegistry = new ListChangeRegistry();
        mCallCount = 0;
    }

    @After
    public void tearDown() throws Exception {
        mListChangeRegistry = null;
    }

    @Test
    public void testNotifyChangedAll() {
        OnListChangedCallback listChangedCallback = new OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                mCallCount++;
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int start, int count) {
                fail("onItemRangeChanged should not be called");
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int start, int count) {
                fail("onItemRangeInserted should not be called");
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int from, int to, int count) {
                fail("onItemRangeMoved should not be called");
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int start, int count) {
                fail("onItemRangeRemoved should not be called");
            }
        };

        mListChangeRegistry.add(listChangedCallback);
        assertEquals(0, mCallCount);
        mListChangeRegistry.notifyChanged(null);
        assertEquals(1, mCallCount);
    }

    @Test
    public void testNotifyChanged() {
        final int expectedStart = 10;
        final int expectedCount = 3;

        OnListChangedCallback listChangedCallback = new OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                fail("onChanged should not be called");
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int start, int count) {
                assertEquals(expectedStart, start);
                assertEquals(expectedCount, count);
                mCallCount++;
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int start, int count) {
                fail("onItemRangeInserted should not be called");
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int from, int to, int count) {
                fail("onItemRangeMoved should not be called");
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int start, int count) {
                fail("onItemRangeRemoved should not be called");
            }
        };

        mListChangeRegistry.add(listChangedCallback);
        assertEquals(0, mCallCount);
        mListChangeRegistry.notifyChanged(null, expectedStart, expectedCount);
        assertEquals(1, mCallCount);
    }

    @Test
    public void testNotifyInserted() {
        final int expectedStart = 10;
        final int expectedCount = 3;

        OnListChangedCallback listChangedCallback = new OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                fail("onChanged should not be called");
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int start, int count) {
                fail("onItemRangeChanged should not be called");
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int start, int count) {
                assertEquals(expectedStart, start);
                assertEquals(expectedCount, count);
                mCallCount++;
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int from, int to, int count) {
                fail("onItemRangeMoved should not be called");
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int start, int count) {
                fail("onItemRangeRemoved should not be called");
            }
        };

        mListChangeRegistry.add(listChangedCallback);
        assertEquals(0, mCallCount);
        mListChangeRegistry.notifyInserted(null, expectedStart, expectedCount);
        assertEquals(1, mCallCount);
    }

    @Test
    public void testNotifyMoved() {
        final int expectedFrom = 10;
        final int expectedTo = 100;
        final int expectedCount = 3;

        OnListChangedCallback listChangedCallback = new OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                fail("onChanged should not be called");
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int start, int count) {
                fail("onItemRangeChanged should not be called");
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int start, int count) {
                fail("onItemRangeInserted should not be called");
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int from, int to, int count) {
                assertEquals(expectedFrom, from);
                assertEquals(expectedTo, to);
                assertEquals(expectedCount, count);
                mCallCount++;
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int start, int count) {
                fail("onItemRangeRemoved should not be called");
            }
        };

        mListChangeRegistry.add(listChangedCallback);
        assertEquals(0, mCallCount);
        mListChangeRegistry.notifyMoved(null, expectedFrom, expectedTo, expectedCount);
        assertEquals(1, mCallCount);
    }

    @Test
    public void testNotifyRemoved() {
        final int expectedStart = 10;
        final int expectedCount = 3;

        OnListChangedCallback listChangedCallback = new OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                fail("onChanged should not be called");
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int start, int count) {
                fail("onItemRangeChanged should not be called");
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int start, int count) {
                fail("onItemRangeInserted should not be called");
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int from, int to, int count) {
                fail("onItemRangeMoved should not be called");
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int start, int count) {
                assertEquals(expectedStart, start);
                assertEquals(expectedCount, count);
                mCallCount++;
            }
        };

        mListChangeRegistry.add(listChangedCallback);
        assertEquals(0, mCallCount);
        mListChangeRegistry.notifyRemoved(null, expectedStart, expectedCount);
        assertEquals(1, mCallCount);
    }
}
