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

package android.databinding.multimoduletestapp;

import android.databinding.testlibrary.ObservableInLibrary;

import android.app.Application;
import android.databinding.Observable;
import android.test.ApplicationTestCase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import android.databinding.multimoduletestapp.databinding.HasIncludeFromLibBinding;
import android.databinding.testlibrary.databinding.IncludedInAppLayoutBinding;
import com.android.databinding.oldversion.databinding.IncludedInAppFromLibraryBinding;
/**
 * There is nothing to assert here. If this test compiles, it passes
 */
public class IncludedLayoutsAccessTest extends ApplicationTestCase<Application> {

    public IncludedLayoutsAccessTest() {
        super(Application.class);
    }

    public void neverRun() {
        HasIncludeFromLibBinding binding = null;
        IncludedInAppLayoutBinding included = binding.included;
        IncludedInAppFromLibraryBinding includedFromMvn = binding.includedFromMvn;
    }
}
