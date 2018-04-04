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

package android.databinding;


import android.databinding.testapp.R;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DataBindingMapperTest {
    @Test
    public void testNotDataBindingId() {
        View view = new View(InstrumentationRegistry.getTargetContext());
        view.setTag("layout/unexpected");
        DataBinderMapper mapper = new DataBinderMapperImpl();
        ViewDataBinding binding = mapper.getDataBinder(null, view, 1);
        assertNull(binding);
    }

    @Test
    public void testInvalidView() {
        View view = new View(InstrumentationRegistry.getTargetContext());
        view.setTag("layout/unexpected");
        DataBinderMapper mapper = new DataBinderMapperImpl();
        Throwable error = null;
        try {
            mapper.getDataBinder(null, view, R.layout.multi_res_layout);
        } catch (Throwable t) {
            error = t;
        }
        assertNotNull(error);
        assertEquals("The tag for multi_res_layout is invalid. Received: layout/unexpected",
                error.getMessage());

    }
}
