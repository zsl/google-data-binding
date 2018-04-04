/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.databinding.testapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.testapp.databinding.NoDataElementBinding;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BindingLinearLayout extends LinearLayout {
    boolean mAlreadyInflated;

    public BindingLinearLayout(Context context) {
        super(context);
    }

    public BindingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    public BindingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public BindingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!mAlreadyInflated) {
            mAlreadyInflated = true;
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            NoDataElementBinding.inflate(layoutInflater, this, true);
        }
    }

    public void setNothing(int nothing) {}
}
