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

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.testapp.adapter.InstanceAdapter;
import android.databinding.testapp.adapter.NameClashAdapter;
import android.databinding.testapp.adapter.NameClashAdapter.MyAdapter;
import android.databinding.testapp.databinding.IncludeInstanceAdapterBinding;
import android.databinding.testapp.databinding.InstanceAdapterBinding;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class InstanceAdapterTest extends BaseDataBinderTest<InstanceAdapterBinding> {
    public InstanceAdapterTest() {
        super(InstanceAdapterBinding.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    private void initNormal() {
        DataBindingUtil.setDefaultComponent(new TestComponent() {
            private InstanceAdapter mInstanceAdapter = new InstanceAdapter("Hello %s %s %s %s");

            @Override
            public MyAdapter getMyAdapter1() {
                return null;
            }

            @Override
            public android.databinding.testapp.adapter2.NameClashAdapter.MyAdapter getMyAdapter2() {
                return null;
            }

            @Override
            public NameClashAdapter getNameClashAdapter1() {
                return null;
            }

            @Override
            public android.databinding.testapp.adapter2.NameClashAdapter getNameClashAdapter2() {
                return null;
            }

            @Override
            public InstanceAdapter getInstanceAdapter() {
                return mInstanceAdapter;
            }
        });
        initBinder();
        mBinder.executePendingBindings();
    }

    @Test
    @UiThreadTest
    public void testOneAttr() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("Hello World foo bar baz", mBinder.textView1.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testTwoAttr() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("Hello World baz foo bar", mBinder.textView2.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testOneAttrOld() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("Hello null World foo bar", mBinder.textView3.getText().toString());
        mBinder.setStr("Android");
        mBinder.executePendingBindings();
        assertEquals("Hello World Android foo bar", mBinder.textView3.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testTwoAttrOld() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("Hello null baz World baz", mBinder.textView4.getText().toString());
        mBinder.setStr("Android");
        mBinder.executePendingBindings();
        assertEquals("Hello World baz Android baz", mBinder.textView4.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testRequiredBinding() {
        try {
            InstanceAdapterBinding.inflate(getActivity().getLayoutInflater(), null);
            fail("Binding should fail if a required BindingAdapter is missing.");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    @UiThreadTest
    public void testInclude() {
        initNormal();
        DataBindingComponent component = DataBindingUtil.getDefaultComponent();
        DataBindingUtil.setDefaultComponent(null);
        IncludeInstanceAdapterBinding binding = IncludeInstanceAdapterBinding.inflate(getActivity().getLayoutInflater(), component);
        binding.setStr("World");
        binding.executePendingBindings();
        assertEquals("Hello World foo bar baz", binding.includedLayout.textView1.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testViewStub() {
        initNormal();
        DataBindingComponent component = DataBindingUtil.getDefaultComponent();
        DataBindingUtil.setDefaultComponent(null);
        IncludeInstanceAdapterBinding binding = DataBindingUtil.setContentView(getActivity(),
                R.layout.include_instance_adapter, component);
        binding.setStr("World");
        binding.executePendingBindings();
        binding.viewStub.getViewStub().inflate();
        TextView view = (TextView) binding.viewStub.getRoot().findViewById(R.id.textView1);
        assertEquals("Hello World foo bar baz", view.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testOneAttrWithComponentStatic() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("World component", mBinder.textView6.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testOneAttrWithComponentInstance() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("Hello World component bar baz", mBinder.textView7.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testTwoAttrsWithComponentInstance() {
        initNormal();
        mBinder.setStr("World");
        mBinder.executePendingBindings();
        assertEquals("Hello World foo component bar", mBinder.textView8.getText().toString());
    }
}
