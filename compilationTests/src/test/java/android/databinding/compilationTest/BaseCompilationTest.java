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
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class BaseCompilationTest {
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

    protected void copyResourceTo(String name, File targetFile) throws IOException {
        File directory = targetFile.getParentFile();
        FileUtils.forceMkdir(directory);
        InputStream contents = getClass().getResourceAsStream(name);
        FileOutputStream fos = new FileOutputStream(targetFile);
        IOUtils.copy(contents, fos);
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(contents);
    }

    protected void prepareProject() throws IOException, URISyntaxException {
        // how to get build folder, pass from gradle somehow ?

        if (testFolder.exists()) {
            FileUtils.forceDelete(testFolder);
        }
        FileUtils.forceMkdir(testFolder);
        copyResourceTo("/AndroidManifest.xml", new File(testFolder, "app/src/main/AndroidManifest.xml"));
        copyResourceTo("/project_build.gradle", new File(testFolder, "build.gradle"));
        copyResourceTo("/app_build.gradle", new File(testFolder, "app/build.gradle"));
        copyResourceTo("/settings.gradle", new File(testFolder, "settings.gradle"));
        FileUtils.copyFile(new File("../local.properties"), new File(testFolder, "local.properties"));
        FileUtils.copyFile(new File("../gradlew"), new File(testFolder, "gradlew"));
        FileUtils.copyDirectory(new File("../gradle"), new File(testFolder, "gradle"));
    }

    protected CompilationResult runGradle(String params) throws IOException, InterruptedException {
        setExecutable();
        File pathToExecutable = new File(testFolder, "gradlew");
        ProcessBuilder builder = new ProcessBuilder(pathToExecutable.getAbsolutePath(), params);
        builder.environment().putAll(System.getenv());
        builder.directory(testFolder); // this is where you set the root folder for the executable to run with
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
