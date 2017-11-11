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

import android.databinding.testapp.databinding.PlainViewGroupBinding;
import android.databinding.testapp.databinding.TwoWayBinding;
import android.databinding.testapp.vo.TwoWayBindingObject;
import android.view.View;
import android.view.ViewGroup;

public class ReferenceRegistrationTest extends BaseDataBinderTest<PlainViewGroupBinding> {
    public ReferenceRegistrationTest() {
        super(PlainViewGroupBinding.class);
    }

    // Make sure that rebind() works after unbind() from detaching...
    public void testRebinding() throws Throwable {
        initBinder();
        final TwoWayBindingObject obj = new TwoWayBindingObject(getActivity());
        final TwoWayBinding[] bindings = new TwoWayBinding[1];
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                bindings[0] = TwoWayBinding.inflate(
                        getActivity().getLayoutInflater(), mBinder.container, true);
                bindings[0].setObj(obj);
                bindings[0].executePendingBindings();
            }
        });
        final TwoWayBinding binding = bindings[0];

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup viewGroup = (ViewGroup) binding.getRoot();
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    viewGroup.getChildAt(i).setVisibility(View.GONE);
                }
                binding.listView.setVisibility(View.VISIBLE);
            }
        });

        waitForUISync();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertEquals(0, obj.selectedItemPosition.get());
                assertEquals(0, binding.listView.getSelectedItemPosition());
                binding.listView.setSelection(1);
            }
        });
        waitForUISync();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, binding.listView.getSelectedItemPosition());
                assertEquals(1, obj.selectedItemPosition.get());
            }
        });

        // now detach the View
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBinder.container.removeAllViews();
            }
        });

        // and make a change...
        obj.charField.set('b');
        waitForUISync();

        // obj.charField should be detached.
        obj.charField.set('e');
        waitForUISync();

        assertFalse("e".equals(binding.convertChar.getText().toString()));

        // now reattach
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBinder.container.addView(binding.getRoot());
            }
        });

        waitForUISync();
        assertEquals("e", binding.convertChar.getText().toString());

        // now make sure that binding still works
        obj.charField.set('c');

        waitForUISync();
        assertEquals("c", binding.convertChar.getText().toString());

        // try it again in case the first was just the executeBindings working
        obj.charField.set('d');
        waitForUISync();
        assertEquals("d", binding.convertChar.getText().toString());

        // now make sure two-way works
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.listView.setSelection(2);
            }
        });

        waitForUISync();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertEquals(2, binding.listView.getSelectedItemPosition());
                assertEquals(2, obj.selectedItemPosition.get());
            }
        });
    }
}
