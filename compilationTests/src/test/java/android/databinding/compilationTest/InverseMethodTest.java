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

package android.databinding.compilationTest;

import android.databinding.tool.CompilerChef;
import android.databinding.tool.processing.ErrorMessages;
import android.databinding.tool.processing.ScopedErrorReport;
import android.databinding.tool.processing.ScopedException;
import android.databinding.tool.reflection.InjectedClass;
import android.databinding.tool.reflection.ModelClass;
import android.databinding.tool.reflection.ModelMethod;
import android.databinding.tool.reflection.java.JavaAnalyzer;
import android.databinding.tool.store.Location;

import com.google.common.base.Joiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class InverseMethodTest extends BaseCompilationTest {

    @Test
    public void testInverseMethodWrongParameterType() throws Throwable {
        testErrorForMethod("InverseMethod_WrongParameterType", 23, "Could not find inverse " +
                "method: public static int wrongParameterType(java.lang.String)");
    }

    @Test
    public void testInverseMethodNoArg() throws Throwable {
        testErrorForMethod("InverseMethod_NoArg", 23, "@InverseMethods must have at least one " +
                "parameter.");
    }

    @Test
    public void testInverseMethodNoInverse() throws Throwable {
        testErrorForMethod("InverseMethod_NoInverse", 23, "@InverseMethod must supply a value " +
                "containing the name of the method to call when going from View value to bound " +
                "value");
    }

    @Test
    public void testInverseMethodNonPublic() throws Throwable {
        testErrorForMethod("InverseMethod_NonPublic", 23, "@InverseMethods must be associated " +
                "with a public method");
    }

    @Test
    public void testInverseMethodNonPublicInverse() throws Throwable {
        testErrorForMethod("InverseMethod_NonPublicInverse", 23, "InverseMethod must be declared " +
                "public 'notPublicIntToString(int)'");
    }

    private void testErrorForMethod(String className, int lineNumber, String expectedError)
            throws Throwable {
        prepareProject();
        copyResourceTo(
                "/android/databinding/compilationTest/badJava/" + className + ".java",
                "/app/src/main/java/android/databinding/compilationTest/badJava/" + className + ".java");
        CompilationResult result = runGradle("assembleDebug", "--stacktrace");
        assertNotEquals(0, result.resultCode);
        String error = getErrorLine(result.error);
        assertNotNull("Couldn't find error in \n" + result.error, error);
        File errorFile = new File(testFolder,
                "/app/src/main/java/android/databinding/compilationTest/badJava/" +
                        className +  ".java");
        assertEquals(errorFile.getCanonicalPath() + ":" + lineNumber + ": error: " + expectedError,
                error);
    }

    private static String getErrorLine(String err) {
        String[] lines = err.split("\n");
        for (String line : lines) {
            if (line.contains("error:")) {
                return line.trim();
            }
        }
        return null;
    }
}
