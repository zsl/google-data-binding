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

package android.databinding.tool.writer

import android.databinding.tool.DataBindingCompilerArgs
import android.databinding.tool.ext.L
import android.databinding.tool.ext.N
import android.databinding.tool.ext.S
import android.databinding.tool.ext.T
import android.databinding.tool.reflection.ModelAnalyzer
import android.databinding.tool.store.GenClassInfoLog
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.annotation.Generated
import javax.lang.model.element.Modifier

class BindingMapperWriterV2(private val pkg: String,
                            private val appClassName: String,
                            private val genClassInfoLog: GenClassInfoLog,
                            private val compilerArgs: DataBindingCompilerArgs) {
    private val testClassName = "Test$appClassName"

    companion object {
        private val VIEW_DATA_BINDING = ClassName
                .get("android.databinding", "ViewDataBinding")
        private val COMPONENT = ClassName
                .get("android.databinding", "DataBindingComponent")
        private val VIEW = ClassName
                .get("android.view", "View")
        private val OBJECT = ClassName
                .get("java.lang", "Object")
        private val RUNTIME_EXCEPTION = ClassName
                .get("java.lang", "RuntimeException")
        private val ILLEGAL_ARG_EXCEPTION = ClassName
                .get("java.lang", "IllegalArgumentException")
        private val STRING = ClassName
                .get("java.lang", "String")
    }

    private val appTypeSpec = ClassName.bestGuess("$pkg.${this.appClassName}")
    private val generateAsTest = compilerArgs.isTestVariant && compilerArgs.isApp
    private val generateTestOverride = !generateAsTest && compilerArgs.isEnabledForTests
    private val overrideField = FieldSpec.builder(appTypeSpec, "sTestOverride")
            .addModifiers(Modifier.STATIC)
            .build()

    private val rClassMap = mutableMapOf<String, ClassName>()
    val className = if (generateAsTest) {
        "Test$appClassName"
    } else {
        appClassName
    }

    private fun getRClass(pkg: String): ClassName {
        return rClassMap.getOrPut(pkg) {
            ClassName.get(pkg, "R")
        }
    }

    fun write(brWriter: BRWriter): TypeSpec = TypeSpec.classBuilder(className).apply {
        if (generateAsTest) {
            superclass(appTypeSpec)
        }
        if (ModelAnalyzer.getInstance().hasGeneratedAnnotation()) {
            addAnnotation(AnnotationSpec.builder(Generated::class.java).apply {
                addMember("value", S, "Android Data Binding")
            }.build())
        }
        val minSdkField = FieldSpec.builder(TypeName.INT, "TARGET_MIN_SDK",
                Modifier.FINAL, Modifier.STATIC).apply {
            initializer(L, compilerArgs.minApi)
        }.build()
        addField(minSdkField)
        if (generateTestOverride) {
            addField(overrideField)
            addStaticBlock(CodeBlock.builder()
                    .beginControlFlow("try").apply {
                addStatement("$N = ($T) $T.class.getClassLoader().loadClass($S).newInstance()",
                        overrideField, appTypeSpec, appTypeSpec, """$pkg.$testClassName""")
            }.nextControlFlow("catch($T ignored)", ClassName.get(Throwable::class.java))
                    .apply {
                        addStatement("$N = null", overrideField)
                    }
                    .endControlFlow()
                    .build())
        }
        addMethod(generateGetViewDataBinder())
        addMethod(generateGetViewArrayDataBinder())
        addMethod(generateGetLayoutId())
        addMethod(generateConvertBrIdToString())
        addType(generateInnerBrLookup(brWriter))
    }.build()

    private fun generateInnerBrLookup(brWriter: BRWriter) = TypeSpec
            .classBuilder("InnerBrLookup").apply {
        addModifiers(Modifier.PRIVATE, Modifier.STATIC)
        val keysField = FieldSpec.builder(ArrayTypeName.of(STRING), "sKeys").apply {
            addModifiers(Modifier.STATIC, Modifier.FINAL)
            val placeholders = brWriter.indexedProps.joinToString(",") { S }
            val args = listOf(ArrayTypeName.of(STRING), "_all") +
                    brWriter.indexedProps.map { it.value }
            initializer("new $T{$S, $placeholders}", *(args.toTypedArray()))
        }.build()
        addField(keysField)
    }.build()

    private fun generateConvertBrIdToString() = MethodSpec
            .methodBuilder("convertBrIdToString").apply {
        val idParam = ParameterSpec.builder(TypeName.INT, "id").build()
        addParameter(idParam)
        returns(BindingMapperWriterV2.STRING)
        beginControlFlow("if($N < 0 || $N >= InnerBrLookup.sKeys.length)",
                idParam, idParam).apply {
            if (generateTestOverride) {
                beginControlFlow("if($N != null)", overrideField).apply {
                    addStatement("return $N.convertBrIdToString($N)", overrideField, idParam)
                }.endControlFlow()
                addStatement("return null")
            }
        }.endControlFlow()
        addStatement("return InnerBrLookup.sKeys[$N]", idParam)
    }.build()

    private fun generateGetLayoutId() = MethodSpec.methodBuilder("getLayoutId").apply {
        val tagParam = ParameterSpec.builder(STRING, "tag").build()
        addParameter(tagParam)
        returns(TypeName.INT)
        beginControlFlow("if ($N == null)", tagParam).apply {
            addStatement("return 0")
        }.endControlFlow()

        // output looks like
        // switch has code of tag parameter
        //    case <known hash> (known tags grouped by hash)
        //         find matching tag via equals and return

        // String.hashCode is well defined in the API so we can rely on it being the same on
        // the device and the host machine
        addStatement("final $T code = $N.hashCode()", TypeName.INT, tagParam)
        beginControlFlow("switch(code)").apply {
            genClassInfoLog.mappings()
                    .flatMap { mapping ->
                        mapping.value.implementations
                                .map { Pair(it, mapping) }
                    }
                    .groupBy { pair ->
                        (pair.first.tag + "_0").hashCode()
                    }
                    .forEach { code, pairs ->
                        beginControlFlow("case $L:", code).apply {
                            pairs.forEach {
                                val rClass = getRClass(it.second.value.modulePackage)
                                beginControlFlow("if($N.equals($S))",
                                        tagParam, "${it.first.tag}_0").apply {
                                    addStatement("return $T.layout.$L", rClass, it.second.key)
                                }.endControlFlow()
                            }
                            addStatement("break")
                        }.endControlFlow()
                    }
        }.endControlFlow()
        if (generateTestOverride) {
            beginControlFlow("if($N != null)", overrideField).apply {
                addStatement("return $N.getLayoutId($N)", overrideField, tagParam)
            }.endControlFlow()
        }
        addStatement("return 0")
    }.build()

    private fun generateGetViewDataBinder(): MethodSpec {
        return MethodSpec.methodBuilder("getDataBinder").apply {
            addModifiers(Modifier.PUBLIC)
            returns(VIEW_DATA_BINDING)
            val componentParam = ParameterSpec.builder(COMPONENT, "component").build()
            val viewParam = ParameterSpec.builder(VIEW, "view").build()
            val layoutIdParam = ParameterSpec.builder(TypeName.INT, "layoutId").build()
            addParameter(componentParam)
            addParameter(viewParam)
            addParameter(layoutIdParam)
            // output looks like:
            // switch(layoutId)
            //    case known_layout_id
            //             verify, generate impl and return
            beginControlFlow("switch($N)", layoutIdParam).apply {
                genClassInfoLog.mappings().forEach { layoutName, info ->
                    val rClass = getRClass(info.modulePackage)
                    beginControlFlow("case $T.layout.$L :", rClass, layoutName).apply {
                        // we should check the tag to decide which layout we need to inflate
                        // we do it here because it is ok to pass a non-data-binding layout
                        addStatement("final $T tag = $N.getTag()", OBJECT, viewParam)
                        beginControlFlow("if(tag == null)").apply {
                            addStatement("throw new $T($S)", RUNTIME_EXCEPTION,
                                    "view must have a tag")
                        }.endControlFlow()
                        info.implementations.forEach {
                            beginControlFlow("if ($S.equals(tag))",
                                    "${it.tag}_0").apply {
                                val binderTypeName = ClassName.bestGuess(it.qualifiedName)
                                if (it.merge) {
                                    addStatement("return new $T($N, new $T[]{$N})",
                                            binderTypeName, componentParam, VIEW, viewParam)
                                } else {
                                    addStatement("return new $T($N, $N)",
                                            binderTypeName, componentParam, viewParam)
                                }
                            }.endControlFlow()
                        }
                        addStatement("throw new $T($S + tag)", ILLEGAL_ARG_EXCEPTION,
                                "The tag for $layoutName is invalid. Received: ")
                    }.endControlFlow()
                }
            }.endControlFlow()
            if (generateTestOverride) {
                beginControlFlow("if($N != null)", overrideField).apply {
                    addStatement("return $N.getDataBinder($N, $N, $N)",
                            overrideField, componentParam, viewParam, layoutIdParam)
                }.endControlFlow()
            }
            addStatement("return null")
        }.build()
    }

    private fun generateGetViewArrayDataBinder() = MethodSpec.methodBuilder("getDataBinder").apply {
        addModifiers(Modifier.PUBLIC)
        returns(VIEW_DATA_BINDING)
        val componentParam = ParameterSpec.builder(COMPONENT, "component").build()
        val viewParam = ParameterSpec.builder(ArrayTypeName.of(VIEW), "views").build()
        val layoutIdParam = ParameterSpec.builder(TypeName.INT, "layoutId").build()
        addParameter(componentParam)
        addParameter(viewParam)
        addParameter(layoutIdParam)
        // output looks like:
        // switch(layoutId)
        //    case known_layout_id
        //             verify, generate impl and return
        beginControlFlow("switch($N)", layoutIdParam).apply {
            genClassInfoLog.mappings().forEach { layoutName, info ->
                val mergeImpls = info.implementations.filter { it.merge }
                if (mergeImpls.isNotEmpty()) {
                    val rClass = getRClass(info.modulePackage)
                    beginControlFlow("case $T.layout.$L:", rClass, layoutName).apply {
                        // we should check the tag to decide which layout we need to inflate
                        // we do it here because it is ok to pass non-data-binding view.
                        addStatement("final $T tag = $N[0].getTag()", OBJECT, viewParam)
                        beginControlFlow("if(tag == null)").apply {
                            addStatement("throw new $T($S)", RUNTIME_EXCEPTION,
                                    "view must have a tag")
                        }.endControlFlow()

                        mergeImpls.forEach {
                            beginControlFlow("if($S.equals(tag))",
                                    "${it.tag}_0").apply {
                                val binderTypeName = ClassName.bestGuess(it.qualifiedName)
                                addStatement("return new $T($N, $N)",
                                        binderTypeName, componentParam, viewParam)
                            }.endControlFlow()
                        }
                    }.endControlFlow()
                }
            }
        }.endControlFlow()
        if (generateTestOverride) {
            beginControlFlow("if($N != null)", overrideField).apply {
                addStatement("return $N.getDataBinder($N, $N, $N)",
                        overrideField, componentParam, viewParam, layoutIdParam)
            }.endControlFlow()
        }
        addStatement("return null")
    }.build()
}
