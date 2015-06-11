/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.databinding.tool

import android.databinding.tool.expr.Dependency
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.variant.ApplicationVariantData
import java.io.File
import org.gradle.api.file.FileCollection
import android.databinding.tool.writer.JavaFileWriter
import org.gradle.api.Action
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.variant.BaseVariantData
import com.android.build.gradle.internal.variant.LibraryVariantData
import com.android.build.gradle.internal.api.LibraryVariantImpl
import com.android.build.gradle.api.TestVariant
import com.android.build.gradle.internal.variant.TestVariantData
import com.android.build.gradle.internal.api.TestVariantImpl
import com.android.ide.common.res2.ResourceSet
import org.apache.commons.io.IOUtils
import org.gradle.api.artifacts
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.api.logging.LogLevel
import java.io.FileFilter
import java.io.FilenameFilter
import java.util.ArrayList
import javax.inject.Inject
import javax.xml.xpath.XPathFactory
import kotlin.dom.elements
import kotlin.dom.parseXml
import kotlin.properties.Delegates
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtraPropertiesExtension


open class DataBinderPlugin : Plugin<Project> {
    val XPATH_BINDING_CLASS = "/layout/data/@class"
    var logger : Logger by Delegates.notNull()

    inner class GradleFileWriter(var outputBase: String) : JavaFileWriter() {
        override fun writeToFile(canonicalName: String, contents: String) {
            val f = File("$outputBase/${canonicalName.replaceAll("\\.", "/")}.java")
            logD("Asked to write to ${canonicalName}. outputting to:${f.getAbsolutePath()}")
            f.getParentFile().mkdirs()
            f.writeText(contents, "utf-8")
        }
    }

    fun readMyVersion() : String {
        val stream = javaClass.getResourceAsStream("/data_binding_build_info")
        return IOUtils.toString(stream, "utf-8").trim()
    }

    override fun apply(project: Project?) {
        if (project == null) return

        logger = project.getLogger()

        var myVersion = readMyVersion()
        logD("data binding plugin version is ${myVersion}")
        if (myVersion == "") {
            throw IllegalStateException("cannot read version of the plugin :/")
        }

        project.getDependencies().add("compile", "com.android.databinding:library:$myVersion")
        var addAdapters = true
        if (project.hasProperty("ext")) {
            val ext = project.getProperties().get("ext")
            if (ext is ExtraPropertiesExtension) {
                if (ext.has("addDataBindingAdapters")) {
                    addAdapters = ext.get("addDataBindingAdapters") as Boolean
                }
            }
        }
        if (addAdapters) {
            project.getDependencies().add("compile", "com.android.databinding:adapters:$myVersion")
        }
        project.getDependencies().add("provided", "com.android.databinding:compiler:$myVersion")
        project.afterEvaluate {
            createXmlProcessor(project)
        }
    }

    fun logD(s: String) {
        log(LogLevel.INFO, s)
    }
    fun logE(s: String) {
        log(LogLevel.ERROR, s)
    }
    fun log(level : LogLevel, s: String) {
        logger.log(level, "[data binding plugin]: $s")
    }

    fun createXmlProcessor(p: Project) {
        val androidExt = p.getExtensions().getByName("android")
        if (androidExt !is BaseExtension) {
            return
        }
        if (androidExt is AppExtension) {
            createXmlProcessorForApp(p, androidExt)
        } else if (androidExt is LibraryExtension) {
            createXmlProcessorForLibrary(p, androidExt)
        } else {
            logE("unsupported android extension. What is it? ${androidExt}")
            throw RuntimeException("unsupported android extension. What is it? ${androidExt}")
        }
    }

    fun createXmlProcessorForLibrary(project : Project, lib : LibraryExtension) {
        val sdkDir = lib.getSdkDirectory()
        lib.getTestVariants().forEach { variant ->
            logD("test variant $variant. dir name ${variant.getDirName()}")
            val variantData = getVariantData(variant)
            attachXmlProcessor(project, variantData, sdkDir, false)//tests extend apk variant
        }
        lib.getLibraryVariants().forEach { variant ->
            logD("lib variant $variant . dir name ${variant.getDirName()}")
            val variantData = getVariantData(variant)
            attachXmlProcessor(project, variantData, sdkDir, true)
        }
    }

    fun getVariantData(appVariant : LibraryVariant) : LibraryVariantData {
        val clazz = javaClass<LibraryVariantImpl>()
        val field = clazz.getDeclaredField("variantData")
        field.setAccessible(true)
        return field.get(appVariant) as LibraryVariantData
    }

    fun getVariantData(testVariant : TestVariant) : TestVariantData {
        val clazz = javaClass<TestVariantImpl>()
        val field = clazz.getDeclaredField("variantData")
        field.setAccessible(true)
        return field.get(testVariant) as TestVariantData
    }

    fun getVariantData(appVariant : ApplicationVariant) : ApplicationVariantData {
        val clazz = javaClass<ApplicationVariantImpl>()
        val field = clazz.getDeclaredField("variantData")
        field.setAccessible(true)
        return field.get(appVariant) as ApplicationVariantData
    }

    fun createXmlProcessorForApp(project : Project, appExt: AppExtension) {
        val sdkDir = appExt.getSdkDirectory()
        appExt.getTestVariants().forEach { testVariant ->
            val variantData = getVariantData(testVariant)
            attachXmlProcessor(project, variantData, sdkDir, false)
        }
        appExt.getApplicationVariants().forEach { appVariant ->
            val variantData = getVariantData(appVariant)
            attachXmlProcessor(project, variantData, sdkDir, false)
        }
    }

    fun attachXmlProcessor(project : Project, variantData : BaseVariantData<*>, sdkDir : File,
            isLibrary : Boolean) {
        val configuration = variantData.getVariantConfiguration()
        val minSdkVersion = configuration.getMinSdkVersion()
        val generateRTask = variantData.generateRClassTask
        val packageName = generateRTask.getPackageForR()
        val fullName = configuration.getFullName()
        val resourceFolders = arrayListOf(variantData.mergeResourcesTask.getOutputDir())

        val codeGenTargetFolder = File("${project.getBuildDir()}/data-binding-info/${configuration.getDirName()}")
        val writerOutBase = codeGenTargetFolder.getAbsolutePath();
        val fileWriter = GradleFileWriter(writerOutBase)
        val xmlProcessor = LayoutXmlProcessor(packageName, resourceFolders, fileWriter,
                minSdkVersion.getApiLevel(), isLibrary)
        val processResTask = generateRTask
        val xmlOutDir = File("${project.getBuildDir()}/layout-info/${configuration.getDirName()}")
        logD("xml output for ${variantData} is ${xmlOutDir}")
        val layoutTaskName = "dataBindingLayouts${processResTask.getName().capitalize()}"
        val infoClassTaskName = "dataBindingInfoClass${processResTask.getName().capitalize()}"

        var processLayoutsTask : DataBindingProcessLayoutsTask? = null
        project.getTasks().create(layoutTaskName,
                javaClass<DataBindingProcessLayoutsTask>(),
                object : Action<DataBindingProcessLayoutsTask> {
                    override fun execute(task: DataBindingProcessLayoutsTask) {
                        processLayoutsTask = task
                        task.xmlProcessor = xmlProcessor
                        task.sdkDir = sdkDir
                        task.xmlOutFolder = xmlOutDir
                        task.minSdk = minSdkVersion.getApiLevel()

                        logD("TASK adding dependency on ${task} for ${processResTask}")
                        processResTask.dependsOn(task)
                        processResTask.getDependsOn().filterNot { it == task }.forEach {
                            logD("adding dependency on ${it} for ${task}")
                            task.dependsOn(it)
                        }
                        processResTask.doLast {
                            task.writeLayoutXmls()
                        }
                    }
                })
        project.getTasks().create(infoClassTaskName,
                javaClass<DataBindingExportInfoTask>(),
                object : Action<DataBindingExportInfoTask>{
                    override fun execute(task: DataBindingExportInfoTask) {
                        task.dependsOn(processLayoutsTask!!)
                        task.dependsOn(processResTask)
                        task.xmlProcessor = xmlProcessor
                        task.sdkDir = sdkDir
                        task.xmlOutFolder = xmlOutDir
                        task.enableDebugLogs = logger.isEnabled(LogLevel.DEBUG)
                        variantData.registerJavaGeneratingTask(task, codeGenTargetFolder)
                    }
                })

        if (isLibrary) {
            val resourceSets = variantData.mergeResourcesTask.getInputResourceSets()
            val customBindings = getCustomBindings(resourceSets, packageName)
            val packageJarTaskName = "package${fullName.capitalize()}Jar"
            val packageTask = project.getTasks().findByName(packageJarTaskName)
            if (packageTask !is org.gradle.api.tasks.bundling.Jar) {
                throw RuntimeException("cannot find package task in $project $variantData project $packageJarTaskName")
            }
            val excludePattern = "android/databinding/layouts/*.*"
            val appPkgAsClass = packageName.replace('.', '/')
            packageTask.exclude(excludePattern)
            packageTask.exclude("$appPkgAsClass/databinding/*")
            packageTask.exclude("$appPkgAsClass/BR.*")
            packageTask.exclude(xmlProcessor.getInfoClassFullName().replace('.', '/') + ".class")
            customBindings.forEach {
                packageTask.exclude("${it.replace('.', '/')}.class")
            }
            logD("excludes ${packageTask.getExcludes()}")
        }
    }

    fun getCustomBindings(resourceSets : List<ResourceSet>, packageName: String) : List<String> {
        val xPathFactory = XPathFactory.newInstance()
        val xPath = xPathFactory.newXPath()
        val expr = xPath.compile(XPATH_BINDING_CLASS);
        val customBindings = ArrayList<String>()

        resourceSets.forEach { set ->
            set.getSourceFiles().forEach({ res ->
                res.listFiles(object : FileFilter {
                    override fun accept(file: File?): Boolean {
                        return file != null && file.isDirectory() &&
                                file.getName().toLowerCase().startsWith("layout")
                    }
                })?.forEach { layoutDir ->

                    layoutDir.listFiles(object : FileFilter {
                        override fun accept(file: File?): Boolean {
                            return file != null && !file.isDirectory() &&
                                    file.getName().toLowerCase().endsWith(".xml")
                        }
                    })?.forEach { xmlFile: File ->
                        val document = parseXml(xmlFile)
                        val bindingClass = expr.evaluate(document)
                        if (bindingClass != null && !bindingClass.isEmpty()) {
                            if (bindingClass.startsWith('.')) {
                                customBindings.add("${packageName}${bindingClass}")
                            } else if (bindingClass.contains(".")) {
                                customBindings.add(bindingClass)
                            } else {
                                customBindings.add(
                                        "${packageName}.databinding.${bindingClass}")
                            }
                        }
                    }
                }
            })
        }
        return customBindings
    }
}
