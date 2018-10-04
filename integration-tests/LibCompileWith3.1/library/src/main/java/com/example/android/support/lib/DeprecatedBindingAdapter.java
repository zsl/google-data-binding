/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.example.android.support.lib;

import android.databinding.BindingAdapter;
import android.support.v7.widget.CardView;

@SuppressWarnings("unused")
public class DeprecatedBindingAdapter {
    @BindingAdapter("custom_compat_padding")
    public static void setOnSupportCardView(CardView cardView, Boolean value) {
        cardView.setUseCompatPadding(value);
    }
}
