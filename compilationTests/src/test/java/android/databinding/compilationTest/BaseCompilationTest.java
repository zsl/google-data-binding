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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class BaseCompilationTest {
    static Pattern VARIABLES = Pattern.compile("!@\\{([A-Za-z0-9_-]*)}");

    public static final String KEY_MANIFEST_PACKAGE = "PACKAGE";
    public static final String KEY_DEPENDENCIES = "DEPENDENCIES";
    public static final String KEY_SETTINGS_INCLUDES = "SETTINGS_INCLUDES";
    public static final String DEFAULT_APP_PACKAGE = "com.android.databinding.compilationTest.test";

    File testFolder = new File("./build/build-test");

    protected void copyResourceTo(String name, String path) throws IOException {
        copyResourceTo(name, new File(testFolder, path));
    }

    protected void copyResourceDirectory(String name, String targetPath)
            throws URISyntaxException, IOException {
        URL dir = getClass().getResource(name);
        assertNotNull(dir);
        assertEquals("file", dir.getProtocol());
        File folder = new File(dir.toURI());
        assertTrue(folder.isDirectory());
        File target = new File(testFolder, targetPath);
        int len = folder.getAbsolutePath().length() + 1;
        for (File item : FileUtils.listFiles(folder, null, true)) {
            if (item.getAbsolutePath().equals(folder.getAbsolutePath())) {
                continue;
            }
            String resourcePath = item.getAbsolutePath().substring(len);

            copyResourceTo(name + "/" + resourcePath, new File(target, resourcePath));
        }
    }

    @Before
    public void clear() throws IOException {
        if (testFolder.exists()) {
            FileUtils.forceDelete(testFolder);
        }
    }

    protected void copyResourceTo(String name, File targetFile) throws IOException {
        File directory = targetFile.getParentFile();
        FileUtils.forceMkdir(directory);
        InputStream contents = getClass().getResourceAsStream(name);
        FileOutputStream fos = new FileOutputStream(targetFile);
        IOUtils.copy(contents, fos);
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(contents);
    }

    protected static Map<String, String> toMap(String... keysAndValues) {
        assertEquals(0, keysAndValues.length % 2);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i+=2) {
            map.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }

    protected void copyResourceTo(String name, File targetFile, Map<String, String> replacements)
            throws IOException {
        if (replacements.isEmpty()) {
            copyResourceTo(name, targetFile);
        }
        InputStream inputStream = getClass().getResourceAsStream(name);
        final String contents = IOUtils.toString(inputStream);
        IOUtils.closeQuietly(inputStream);

        StringBuilder out = new StringBuilder(contents.length());
        final Matcher matcher = VARIABLES.matcher(contents);
        int location = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (start > location) {
                out.append(contents, location, start);
            }
            final String key = matcher.group(1);
            final String replacement = replacements.get(key);
            if (replacement != null) {
                out.append(replacement);
            }
            location = matcher.end();
        }
        if (location < contents.length()) {
            out.append(contents, location, contents.length());
        }

        FileUtils.writeStringToFile(targetFile, out.toString());
    }

    protected void prepareProject() throws IOException, URISyntaxException {
        prepareApp(null);
    }

    private Map<String, String> addDefaults(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (!map.containsKey(KEY_MANIFEST_PACKAGE)) {
            map.put(KEY_MANIFEST_PACKAGE, DEFAULT_APP_PACKAGE);
        }
        if (!map.containsKey(KEY_SETTINGS_INCLUDES)) {
            map.put(KEY_SETTINGS_INCLUDES, "include ':app'");
        }
        return map;
    }

    protected void prepareApp(Map<String, String> replacements) throws IOException,
            URISyntaxException {
        replacements = addDefaults(replacements);
        // how to get build folder, pass from gradle somehow ?
        FileUtils.forceMkdir(testFolder);
        copyResourceTo("/AndroidManifest.xml", new File(testFolder, "app/src/main/AndroidManifest.xml"), replacements);
        copyResourceTo("/project_build.gradle", new File(testFolder, "build.gradle"), replacements);
        copyResourceTo("/app_build.gradle", new File(testFolder, "app/build.gradle"), replacements);
        copyResourceTo("/settings.gradle", new File(testFolder, "settings.gradle"), replacements);
        FileUtils.copyFile(new File("../local.properties"), new File(testFolder, "local.properties"));
        FileUtils.copyFile(new File("../gradlew"), new File(testFolder, "gradlew"));
        FileUtils.copyDirectory(new File("../gradle"), new File(testFolder, "gradle"));
    }

    protected void prepareModule(String moduleName, String packageName,
            Map<String, String> replacements) throws IOException, URISyntaxException {
        replacements = addDefaults(replacements);
        replacements.put(KEY_MANIFEST_PACKAGE, packageName);
        File moduleFolder = new File(testFolder, moduleName);
        if (moduleFolder.exists()) {
            FileUtils.forceDelete(moduleFolder);
        }
        FileUtils.forceMkdir(moduleFolder);
        copyResourceTo("/AndroidManifest.xml",
                new File(moduleFolder, "src/main/AndroidManifest.xml"), replacements);
        copyResourceTo("/module_build.gradle", new File(moduleFolder, "build.gradle"), replacements);
    }

    protected CompilationResult runGradle(String params) throws IOException, InterruptedException {
        setExecutable();
        File pathToExecutable = new File(testFolder, "gradlew");
        ProcessBuilder builder = new ProcessBuilder(pathToExecutable.getAbsolutePath(), params);
        builder.environment().putAll(System.getenv());
        builder.directory(testFolder);
        //builder.redirectErrorStream(true); // merges error and input streams
        Process process =  builder.start();
        String output = IOUtils.toString(process.getInputStream());
        String error = IOUtils.toString(process.getErrorStream());
        int result = process.waitFor();
        return new CompilationResult(result, output, error);
    }

    private void setExecutable() throws IOException {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        Files.setPosixFilePermissions(Paths.get(new File(testFolder, "gradlew").getAbsolutePath()), perms);
    }


}
