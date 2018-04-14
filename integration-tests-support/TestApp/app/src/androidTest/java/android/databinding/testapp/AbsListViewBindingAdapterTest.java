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

import android.databinding.testapp.databinding.AbsListViewAdapterTestBinding;
import android.databinding.testapp.vo.AbsListViewBindingObject;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AbsListViewBindingAdapterTest
        extends BindingAdapterTestBase<AbsListViewAdapterTestBinding, AbsListViewBindingObject> {

    ListView mView;

    public AbsListViewBindingAdapterTest() {
        super(AbsListViewAdapterTestBinding.class, AbsListViewBindingObject.class,
                R.layout.abs_list_view_adapter_test);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mView = getBinder().view;
    }

    @Test
    public void testListSelector() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            assertEquals(mBindingObject.getListSelector().getColor(),
                    ((ColorDrawable) mView.getSelector()).getColor());

            changeValues();

            assertEquals(mBindingObject.getListSelector().getColor(),
                    ((ColorDrawable) mView.getSelector()).getColor());
        }
    }

    @Test
    public void testScrollingCache() {
        assertEquals(mBindingObject.isScrollingCache(), mView.isScrollingCacheEnabled());

        changeValues();

        assertEquals(mBindingObject.isScrollingCache(), mView.isScrollingCacheEnabled());
    }

    @Test
    public void testSmoothScrollbar() {
        assertEquals(mBindingObject.isSmoothScrollbar(), mView.isSmoothScrollbarEnabled());

        changeValues();

        assertEquals(mBindingObject.isSmoothScrollbar(), mView.isSmoothScrollbarEnabled());
    }
}
