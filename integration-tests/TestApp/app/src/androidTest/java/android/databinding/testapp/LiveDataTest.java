/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.testapp.databinding.LiveDataBinding;
import android.databinding.testapp.databinding.ObservableFieldTestBinding;
import android.databinding.testapp.databinding.PlainViewGroupBinding;
import android.databinding.testapp.vo.LiveDataContainer;
import android.databinding.testapp.vo.LiveDataObject;
import android.databinding.testapp.vo.ObservableFieldBindingObject;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTest extends BaseDataBinderTest<PlainViewGroupBinding> {

    public LiveDataTest() {
        super(PlainViewGroupBinding.class);
    }

    @UiThreadTest
    public void testLiveData() throws Throwable {
        initBinder();
        MyFragment fragment = new MyFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
        fragment.binding.executePendingBindings();
        assertEquals("", fragment.binding.textView1.getText().toString());
        assertEquals("", fragment.binding.textView2.getText().toString());
        assertEquals("", fragment.binding.textView5.getText().toString());

        // Now change the values while the lifecycle owner is active
        fragment.liveDataObject.setValue("Hello");
        fragment.liveDataContainer.liveData.setValue("World");
        fragment.binding.executePendingBindings();
        // make sure the two-way binding loop completes
        fragment.binding.executePendingBindings();
        assertEquals("Hello", fragment.binding.textView1.getText().toString());
        assertEquals("World", fragment.binding.textView2.getText().toString());
        assertEquals("Hello", fragment.binding.textView5.getText().toString());

        // Now change the values while the lifecycle owner is inactive
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .addToBackStack(null)
                .commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();

        fragment.liveDataObject.setValue("Another");
        fragment.liveDataContainer.liveData.setValue("Value");
        fragment.binding.executePendingBindings(); // should do nothing!
        assertEquals("Hello", fragment.binding.textView1.getText().toString());
        assertEquals("World", fragment.binding.textView2.getText().toString());
        assertEquals("Hello", fragment.binding.textView5.getText().toString());

        // Even when it is back in the view hierarchy, it shouldn't trigger a change to
        // the value
        getBinder().container.addView(fragment.binding.getRoot());
        fragment.binding.executePendingBindings(); // should do nothing!
        assertEquals("Hello", fragment.binding.textView1.getText().toString());
        assertEquals("World", fragment.binding.textView2.getText().toString());
        assertEquals("Hello", fragment.binding.textView5.getText().toString());

        // Now let's go back to it being active:
        getBinder().container.removeView(fragment.binding.getRoot());

        getActivity().getSupportFragmentManager().popBackStackImmediate();

        fragment.binding.executePendingBindings();
        assertEquals("Another", fragment.binding.textView1.getText().toString());
        assertEquals("Value", fragment.binding.textView2.getText().toString());
        assertEquals("Another", fragment.binding.textView5.getText().toString());
    }

    @UiThreadTest
    public void testNoLifecycleOwner() throws Throwable {
        initBinder();
        final LiveDataObject liveDataObject = new LiveDataObject();
        final LiveDataContainer liveDataContainer = new LiveDataContainer();
        final LiveDataBinding binding = LiveDataBinding.inflate(getActivity().getLayoutInflater(),
                getBinder().container, true);
        binding.setLiveData(liveDataObject);
        binding.setLiveDataContainer(liveDataContainer);

        binding.executePendingBindings();
        assertEquals("", binding.textView1.getText().toString());
        assertEquals("", binding.textView2.getText().toString());

        liveDataObject.setValue("Hello");
        liveDataContainer.liveData.setValue("World");
        binding.executePendingBindings();

        // No change -- there is no lifecycle owner
        assertEquals("", binding.textView1.getText().toString());
        assertEquals("", binding.textView2.getText().toString());
    }

    @UiThreadTest
    public void testMutableLiveData() throws Throwable {
        initBinder();
        MyFragment fragment = new MyFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
        fragment.binding.executePendingBindings();
        assertEquals("", fragment.binding.textView3.getText().toString());
        assertEquals("", fragment.binding.textView4.getText().toString());
        assertNull(fragment.liveDataObject.getValue());
        assertNull(fragment.liveDataContainer.liveData.getValue());

        fragment.binding.textView3.setText("Hello");
        fragment.binding.textView4.setText("World");

        assertEquals("Hello", fragment.liveDataObject.getValue());
        assertEquals("World", fragment.liveDataContainer.liveData.getValue());
    }

    /**
     * When non-LiveData objects are used with a LifecycleOwner, they should not update when
     * the LifecycleOwner isn't started, but immediately update when the LifecycleOwner has started.
     */
    public void testLifecycleObserverWithField() throws Throwable {
        initBinder();
        final LiveDataBinding[] bindings = new LiveDataBinding[1];
        final ObservableBoolean observableBoolean = new ObservableBoolean();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                bindings[0] = LiveDataBinding.inflate(getActivity().getLayoutInflater(),
                        mBinder.container, true);
                bindings[0].setBValue(observableBoolean);
                bindings[0].executePendingBindings();
            }
        });
        final LiveDataBinding binding = bindings[0];
        assertEquals("false", binding.textView6.getText());

        Fragment fragment = new Fragment();
        binding.setLifecycleOwner(fragment);

        observableBoolean.set(true);

        waitToExecuteBindings();

        // Shouldn't execute bindings
        assertEquals("false", binding.textView6.getText());

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(fragment, "Some Fragment")
                .commit();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.executePendingBindings();
                // now they should be executed
                assertEquals("true", binding.textView6.getText());
            }
        });
    }

    private void waitToExecuteBindings() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(2);
        final View view = mBinder.getRoot();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                if (latch.getCount() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.postOnAnimation(this);
                    } else {
                        view.post(this);
                    }
                }
            }
        };
        runnable.run();
        latch.await(1, TimeUnit.SECONDS);
    }

    public static final class MyFragment extends Fragment {
        public final LiveDataObject liveDataObject = new LiveDataObject();
        public final LiveDataContainer liveDataContainer = new LiveDataContainer();
        public LiveDataBinding binding;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            binding = LiveDataBinding.inflate(inflater, container, false);
            binding.setLiveData(liveDataObject);
            binding.setLiveDataObject(liveDataObject);
            binding.setLiveDataContainer(liveDataContainer);
            binding.setLifecycleOwner(this);
            return binding.getRoot();
        }
    }
}
