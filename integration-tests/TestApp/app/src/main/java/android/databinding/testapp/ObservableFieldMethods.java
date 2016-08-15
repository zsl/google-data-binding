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
package android.databinding.testapp;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

public class ObservableFieldMethods {
    public static ObservableBoolean makeObservable(boolean val) {
        return new ObservableBoolean(val);
    }

    public static ObservableField<ObservableField<String>> crazyNested(String val) {
        return new ObservableField(new ObservableField(val));
    }
}
