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

package android.databinding;

import android.databinding.tool.LayoutXmlProcessor;
import android.databinding.tool.util.L;
import android.databinding.tool.util.Preconditions;
import android.databinding.tool.util.StringUtils;
import android.databinding.tool.writer.JavaFileWriter;

import android.databinding.cli.ProcessXmlOptions;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class AndroidDataBinding {
    public static void main(String[] args)
            throws Throwable {
        ProcessXmlOptions processXmlOptions = new ProcessXmlOptions();
        final JCommander jCommander = new JCommander(processXmlOptions);
        try {
            jCommander.parse(args);
        } catch (ParameterException ex) {
            jCommander.usage();
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        doRun(processXmlOptions);
    }

    public static void doRun(ProcessXmlOptions processXmlOptions) throws Throwable {
        System.out.println(processXmlOptions);
        LayoutXmlProcessor processor = createXmlProcessor(processXmlOptions);
        LayoutXmlProcessor.ResourceInput input = new LayoutXmlProcessor.ResourceInput(
                false,
                processXmlOptions.getResInput(),
                processXmlOptions.getResOutput()
        );
        L.setDebugLog(true);
        processor.processResources(input);
        if (processXmlOptions.shouldZipLayoutInfo()) {
            File outZip = new File(processXmlOptions.getLayoutInfoOutput(),
                    "layout-info.zip");
            FileUtils.forceMkdir(processXmlOptions.getLayoutInfoOutput());
            ZipFileWriter zfw = new ZipFileWriter(outZip);
            processor.writeLayoutInfoFiles(processXmlOptions.getLayoutInfoOutput(), zfw);
            zfw.close();
        } else {
            processor.writeLayoutInfoFiles(processXmlOptions.getLayoutInfoOutput());
        }
    }

    private static LayoutXmlProcessor createXmlProcessor(ProcessXmlOptions processXmlOptions) {
        final ExecFileWriter fileWriter = new ExecFileWriter(processXmlOptions.getResOutput());
        return new LayoutXmlProcessor(
                processXmlOptions.getAppId(),
                fileWriter,
                processXmlOptions.getMinSdk(),
                processXmlOptions.isLibrary(),
                new MyFileLookup()
        );
    }

    static class MyFileLookup implements LayoutXmlProcessor.OriginalFileLookup {

        @Override
        public File getOriginalFileFor(File file) {
            return file;
        }
    }

    static class ZipFileWriter extends JavaFileWriter {
        ZipOutputStream zos;

        public ZipFileWriter(File outZipFile) throws FileNotFoundException {
            FileOutputStream fos;fos = new FileOutputStream(outZipFile);
            zos = new ZipOutputStream(fos);
        }

        @Override
        public void writeToFile(String canonicalName, String contents) {
            throw new RuntimeException("this is only for files not classes");
        }

        @Override
        public void writeToFile(File exactPath, String contents) {
            ZipEntry entry = new ZipEntry(exactPath.getName());
            try {
                zos.putNextEntry(entry);
                zos.write(contents.getBytes(Charsets.UTF_16));
                zos.closeEntry();
            } catch (Throwable t) {
                L.e(t, "cannot write zip file. Filed on %s", exactPath);
            }
        }

        public void close() throws IOException {
            zos.close();
        }
    }

    static class ExecFileWriter extends JavaFileWriter {

        private final File base;

        public ExecFileWriter(File base) {
            this.base = base;
        }

        @SuppressWarnings("Duplicates")
        @Override
        public void writeToFile(String canonicalName, String contents) {
            String asPath = canonicalName.replace('.', '/');
            File f = new File(base, asPath + ".java");
            writeToFile(f, contents);
        }
    }
}
