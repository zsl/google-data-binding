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

package android.databinding.testapp.adapter;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.BindingAdapter;
import android.view.View;

/**
 * Previous versions allowed binding adapters that took ObservableFields. This tests to
 * ensure that they work.
 * TODO: Remove this after support for ObservableFields in BindingAdapters has been removed.
 * This should probably be in Android Studio 2.4
 */
public class ObservableFieldAdapters {
    @BindingAdapter("obsbool1")
    public static void setObservableBoolean(View view, ObservableBoolean obsbool1) {
        boolean val = obsbool1 == null ? false : obsbool1.get();
        view.setEnabled(val);
    }

    @BindingAdapter({"obsbool2", "obsbool3"})
    public static void setMultipleObservableBooleans(View view, ObservableBoolean obsbool2,
            ObservableBoolean obsbool3) {
        boolean val2 = obsbool2 == null ? false : obsbool2.get();
        view.setEnabled(val2);
        boolean val3 = obsbool3 == null ? false : obsbool3.get();
        view.setFocusable(val3);
    }
}
