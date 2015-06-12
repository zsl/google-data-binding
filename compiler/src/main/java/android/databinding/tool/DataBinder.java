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

package android.databinding.tool;

import android.databinding.tool.store.ResourceBundle;
import android.databinding.tool.util.L;
import android.databinding.tool.writer.JavaFileWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The main class that handles parsing files and generating classes.
 */
public class DataBinder {
    List<LayoutBinder> mLayoutBinders = new ArrayList<LayoutBinder>();

    private JavaFileWriter mFileWriter;

    Set<String> writtenClasses = new HashSet<String>();

    public DataBinder(ResourceBundle resourceBundle) {
        L.d("reading resource bundle into data binder");
        for (Map.Entry<String, List<ResourceBundle.LayoutFileBundle>> entry :
                resourceBundle.getLayoutBundles().entrySet()) {
            for (ResourceBundle.LayoutFileBundle bundle : entry.getValue()) {
                mLayoutBinders.add(new LayoutBinder(bundle));
            }
        }
    }
    public List<LayoutBinder> getLayoutBinders() {
        return mLayoutBinders;
    }
    
    public void writerBaseClasses(boolean isLibrary) {
        for (LayoutBinder layoutBinder : mLayoutBinders) {
            if (isLibrary || layoutBinder.hasVariations()) {
                String className = layoutBinder.getClassName();
                String canonicalName = layoutBinder.getPackage() + "." + className;
                if (writtenClasses.contains(canonicalName)) {
                    continue;
                }
                L.d("writing data binder base %s", canonicalName);
                mFileWriter.writeToFile(canonicalName, layoutBinder.writeViewBinderBaseClass());
                writtenClasses.add(canonicalName);
            }
        }
    }
    
    public void writeBinders(int minSdk) {
        for (LayoutBinder layoutBinder : mLayoutBinders) {
            String className = layoutBinder.getImplementationName();
            String canonicalName = layoutBinder.getPackage() + "." + className;
            L.d("writing data binder %s", canonicalName);
            writtenClasses.add(canonicalName);
            mFileWriter.writeToFile(canonicalName, layoutBinder.writeViewBinder(minSdk));
        }
    }

    public Set<String> getWrittenClassNames() {
        return writtenClasses;
    }

    public void setFileWriter(JavaFileWriter fileWriter) {
        mFileWriter = fileWriter;
    }

    public JavaFileWriter getFileWriter() {
        return mFileWriter;
    }
}
