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
package android.databinding.testapp.vo;

import android.arch.lifecycle.MutableLiveData;

public class LiveDataContainer {
    private int value = 0;
    public final MutableLiveData<String> liveData = new MutableLiveData<>();
    public final MutableLiveData<LiveDataContainer> contained = new MutableLiveData<>();

    public String getValue(String data) {
        value++;
        return String.valueOf(value);
    }
}
