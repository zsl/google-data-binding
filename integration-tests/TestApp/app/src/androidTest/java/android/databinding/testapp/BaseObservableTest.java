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

import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.testapp.databinding.BasicBindingBinding;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BaseObservableTest extends BaseDataBinderTest<BasicBindingBinding> {
    private BaseObservable mObservable;
    private ArrayList<Integer> mNotifications = new ArrayList<>();
    private OnPropertyChangedCallback mCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            assertEquals(mObservable, observable);
            mNotifications.add(i);
        }
    };

    public BaseObservableTest() {
        super(BasicBindingBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        mNotifications.clear();
        mObservable = new BaseObservable();
        initBinder(null);
    }

    @Test
    public void testAddCallback() {
        mObservable.notifyChange();
        assertTrue(mNotifications.isEmpty());
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        assertFalse(mNotifications.isEmpty());
    }

    @Test
    public void testRemoveCallback() {
        // test there is no exception when the Callback isn't there
        mObservable.removeOnPropertyChangedCallback(mCallback);

        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        mNotifications.clear();
        mObservable.removeOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        assertTrue(mNotifications.isEmpty());

        // test there is no exception when the Callback isn't there
        mObservable.removeOnPropertyChangedCallback(mCallback);
    }

    @Test
    public void testNotifyChange() {
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyChange();
        assertEquals(1, mNotifications.size());
        assertEquals(0, (int) mNotifications.get(0));
    }

    @Test
    public void testNotifyPropertyChanged() {
        final int expectedId = 100;
        mObservable.addOnPropertyChangedCallback(mCallback);
        mObservable.notifyPropertyChanged(expectedId);
        assertEquals(1, mNotifications.size());
        assertEquals(expectedId, (int) mNotifications.get(0));
    }
}
