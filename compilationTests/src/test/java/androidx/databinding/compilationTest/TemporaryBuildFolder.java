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

package androidx.databinding.compilationTest;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * A rule that creates a temporary folder for each test.
 * It is similar to Junit4's TemporaryFile rule but does not delete the temporary file if the
 * test fails.
 */
public class TemporaryBuildFolder extends TestWatcher {
    private File mFile;
    private File mParent;
    private boolean mKeepOnFailure;

    public TemporaryBuildFolder(File parent) {
        this(parent, false);
    }

    public TemporaryBuildFolder(File parent, boolean keepOnFailure) {
        mParent = parent;
        mKeepOnFailure = keepOnFailure;
    }

    public File getFolder() {
        return mFile;
    }

    @Override
    protected void starting(Description description) {
        String className = description.getClassName();
        String methodName = description.getMethodName();
        String fileName = methodName + "-" + className + "-" +
                UUID.randomUUID().toString().substring(0, 8);
        mFile = new File(mParent, fileName.replaceAll("\\W+", "-"));
        try {
            FileUtils.forceMkdir(mFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.starting(description);
    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);
        // clean the directory since it succeeded
        FileUtils.deleteQuietly(mFile);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (mKeepOnFailure) {
            System.out.println("To re-run this, go to:" + mFile.getAbsolutePath());
        } else {
            FileUtils.deleteQuietly(mFile);
        }

        super.failed(e, description);
    }
}
