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
package android.databinding.testapp.vo;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.testapp.BR;
import android.databinding.testapp.R;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.List;

public class AbsSpinnerBindingObject extends BindingAdapterBindingObject {
    @Bindable
    private CharSequence[] mEntries = {
            "hello",
            "world",
    };

    private static final CharSequence[] CHANGED_VALUES = {
            "goodbye",
            "cruel",
            "world"
    };

    private ObservableList<String> mList = new ObservableArrayList<String>();

    private SpinnerAdapter mAdapter;

    private int mSelectedItemPosition;

    public AbsSpinnerBindingObject() {
        mList.add("Hello");
        mList.add("World");
    }

    public void setContext(Context context) {
        mAdapter = new ArrayAdapter<>(context, R.layout.simple_text, R.id.textView, CHANGED_VALUES);
        notifyPropertyChanged(BR.adapter);
    }

    public CharSequence[] getEntries() {
        return mEntries;
    }

    public void changeValues() {
        mEntries = CHANGED_VALUES;
        notifyChange();
    }

    public List<String> getList() {
        return mList;
    }

    @Bindable
    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    public void setSelectedItemPosition(int pos) {
        mSelectedItemPosition = pos;
        notifyPropertyChanged(BR.selectedItemPosition);
    }

    @Bindable
    public SpinnerAdapter getAdapter() {
        return mAdapter;
    }
}
