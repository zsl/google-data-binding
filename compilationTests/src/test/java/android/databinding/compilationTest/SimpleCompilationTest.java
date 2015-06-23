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

package android.databinding.compilationTest;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SimpleCompilationTest extends BaseCompilationTest {

    @Test
    public void listTasks() throws IOException, URISyntaxException, InterruptedException {
        prepareProject();
        CompilationResult result = runGradle("tasks");
        assertEquals(0, result.resultCode);
        assertTrue("there should not be any errors", StringUtils.isEmpty(result.error));
        assertTrue("Test sanity, empty project tasks",
                result.resultContainsText("All tasks runnable from root project"));
    }

    @Test
    public void testEmptyCompilation() throws IOException, URISyntaxException, InterruptedException {
        prepareProject();
        CompilationResult result = runGradle("assembleDebug");
        assertEquals(0, result.resultCode);
        assertTrue("there should not be any errors " + result.error, StringUtils.isEmpty(result.error));
        assertTrue("Test sanity, should compile fine",
                result.resultContainsText("BUILD SUCCESSFUL"));
    }

    @Test
    public void testUndefinedVariable() throws IOException, URISyntaxException,
            InterruptedException {
        prepareProject();
        copyResourceTo("/layout/undefined_variable_binding.xml", "/app/src/main/res/layout/broken.xml");
        CompilationResult result = runGradle("assembleDebug");
        assertNotEquals(0, result.resultCode);
        assertTrue("Undefined variable",
                result.errorContainsText("Identifiers must have user defined types from the XML file. myVariable is missing it"));
    }

    @Test
    public void testSingleModule() throws IOException, URISyntaxException, InterruptedException {
        prepareApp(toMap(KEY_DEPENDENCIES, "compile project(':module1')",
                KEY_SETTINGS_INCLUDES, "include ':app'\ninclude ':module1'"));
        prepareModule("module1", "com.example.module1", toMap());
        copyResourceTo("/layout/basic_layout.xml", "/module1/src/main/res/layout/module_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/app_layout.xml");
        CompilationResult result = runGradle("assembleDebug");
        assertEquals(result.error, 0, result.resultCode);
    }

    @Test
    public void testTwoLevelDependency() throws IOException, URISyntaxException, InterruptedException {
        prepareApp(toMap(KEY_DEPENDENCIES, "compile project(':module1')",
                KEY_SETTINGS_INCLUDES, "include ':app'\ninclude ':module1'\n"
                        + "include ':module2'"));
        prepareModule("module1", "com.example.module1", toMap(KEY_DEPENDENCIES,
                "compile project(':module2')"));
        prepareModule("module2", "com.example.module2", toMap());
        copyResourceTo("/layout/basic_layout.xml", "/module2/src/main/res/layout/module2_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/module1/src/main/res/layout/module1_layout.xml");
        copyResourceTo("/layout/basic_layout.xml", "/app/src/main/res/layout/app_layout.xml");
        CompilationResult result = runGradle("assembleDebug");
        assertEquals(result.error, 0, result.resultCode);
    }
}
