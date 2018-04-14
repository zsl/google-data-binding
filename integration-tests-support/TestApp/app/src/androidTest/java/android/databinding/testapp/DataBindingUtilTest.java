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

import android.databinding.DataBindingUtil;
import android.databinding.testapp.databinding.BasicBindingBinding;
import android.databinding.testapp.databinding.CenteredContentBinding;
import android.databinding.testapp.databinding.MergeLayoutBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DataBindingUtilTest {
    @Rule
    public final ActivityTestRule<TestActivity> rule = new ActivityTestRule<>(TestActivity.class);

    private TestActivity getActivity() {
        return rule.getActivity();
    }

    @Test
    @UiThreadTest
    public void testFindBinding() {
        BasicBindingBinding binding = BasicBindingBinding.inflate(getActivity().getLayoutInflater());
        assertEquals(binding, DataBindingUtil.findBinding(binding.textView));
        assertEquals(binding, DataBindingUtil.findBinding(binding.getRoot()));
        ViewGroup root = (ViewGroup) binding.getRoot();
        getActivity().getLayoutInflater().inflate(R.layout.basic_binding, root, true);
        View inflated = root.getChildAt(1);
        assertNull(DataBindingUtil.findBinding(inflated));
        BasicBindingBinding innerBinding = DataBindingUtil.bind(inflated);
        assertEquals(innerBinding, DataBindingUtil.findBinding(inflated));
        assertEquals(innerBinding, DataBindingUtil.findBinding(innerBinding.textView));
    }

    @Test
    @UiThreadTest
    public void testGetBinding() {
        BasicBindingBinding binding = BasicBindingBinding.inflate(getActivity().getLayoutInflater());
        assertNull(DataBindingUtil.getBinding(binding.textView));
        assertEquals(binding, DataBindingUtil.getBinding(binding.getRoot()));
    }

    @Test
    @UiThreadTest
    public void testSetContentView() {
        CenteredContentBinding binding = DataBindingUtil.setContentView(getActivity(),
                R.layout.centered_content);
        assertNotNull(binding);
        LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        assertEquals(LayoutParams.WRAP_CONTENT, layoutParams.width);
        assertEquals(LayoutParams.WRAP_CONTENT, layoutParams.height);
    }

    @Test
    @UiThreadTest
    public void testBind() {
        getActivity().setContentView(R.layout.basic_binding);
        ViewGroup content = getActivity().findViewById(android.R.id.content);
        assertEquals(1, content.getChildCount());
        View view = content.getChildAt(0);
        BasicBindingBinding binding = DataBindingUtil.bind(view);
        assertNotNull(binding);
        assertEquals(binding, DataBindingUtil.<BasicBindingBinding>bind(view));
    }

    @Test
    @UiThreadTest
    public void testInflate() {
        getActivity().getWindow().getDecorView(); // force a content to exist.
        ViewGroup content = getActivity().findViewById(android.R.id.content);
        BasicBindingBinding binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.basic_binding, content, false);
        assertNotNull(binding);
        assertNotNull(binding.getRoot().getLayoutParams());
        binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.basic_binding, null, false);
        assertNotNull(binding);
        assertNull(binding.getRoot().getLayoutParams());

        assertNull(DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.plain_layout, null, false));
        MergeLayoutBinding mergeBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                R.layout.merge_layout, content, true);
        assertNotNull(mergeBinding);
        assertNotNull(mergeBinding.innerTextView1);
        assertNotNull(mergeBinding.innerTextView2);

        try {
            DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                    R.layout.merge_layout, content, false);
            fail("Inflating a merge layout without a root should fail");
        } catch (InflateException e) {
            // You can't inflate a merge layout without a root.
        }
    }
}
