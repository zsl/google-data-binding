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

import android.databinding.testapp.databinding.BasicBindingBinding;
import android.support.test.runner.AndroidJUnit4;
import android.util.ArrayMap;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.HashSet;

import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ProcessBindableTest extends BaseDataBinderTest<BasicBindingBinding> {
    private static String[] EXPECTED_BINDING_NAMES = {
            "bindableField1",
            "bindableField2",
            "bindableField3",
            "bindableField4",
            "mbindableField5",
            "bindableField6",
            "bindableField7",
            "bindableField8",
    };

    public ProcessBindableTest() {
        super(BasicBindingBinding.class);
    }

    @Test
    public void testFieldsGenerated() throws IllegalAccessException {
        Field[] fields = BR.class.getFields();

        ArrayMap<String, Integer> fieldValues = new ArrayMap<>();
        int modifiers = PUBLIC | STATIC | FINAL;
        for (Field field : fields) {
            assertTrue(field.getModifiers() == modifiers);
            String name = field.getName();
            fieldValues.put(name, field.getInt(null));
        }

        assertTrue(fieldValues.containsKey("_all"));
        assertEquals(0, (int) fieldValues.get("_all"));
        HashSet<Integer> values = new HashSet<>();
        values.add(0);

        for (String fieldName : EXPECTED_BINDING_NAMES) {
            assertTrue("missing field: " + fieldName, fieldValues.containsKey(fieldName));
            assertFalse(values.contains(fieldValues.get(fieldName)));
            values.add(fieldValues.get(fieldName));
        }
    }
}
