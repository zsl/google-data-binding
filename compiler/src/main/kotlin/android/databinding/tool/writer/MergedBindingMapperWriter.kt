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
import android.databinding.tool.ext.N
import android.databinding.tool.ext.S
import android.databinding.tool.ext.T
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class MergedBindingMapperWriter(private val packages: List<String>,
                                compilerArgs: DataBindingCompilerArgs,
                                private val featurePackages : Set<String>,
                                private val hasV1CompatMapper: Boolean) {
    private val generateAsTest = compilerArgs.isTestVariant && compilerArgs.isApp
    private val generateTestOverride = !generateAsTest && compilerArgs.isEnabledForTests
    private val overrideField = FieldSpec.builder(BindingMapperWriterV2.DATA_BINDER_MAPPER,
            "sTestOverride")
            .addModifiers(Modifier.STATIC)
            .build()

    companion object {
        const val APP_CLASS_NAME = "DataBinderMapperImpl"
        private const val TEST_CLASS_NAME = "Test$APP_CLASS_NAME"
        val MERGED_MAPPER_BASE: ClassName = ClassName.get(
                "android.databinding",
                "MergedDataBinderMapper")
        internal val TEST_OVERRIDE: ClassName = ClassName.get(
                "android.databinding",
                TEST_CLASS_NAME)
    }

    val pkg = "android.databinding"
    val qualifiedName = "$pkg.$APP_CLASS_NAME"

    fun write() = TypeSpec.classBuilder(APP_CLASS_NAME).apply {
        superclass(MERGED_MAPPER_BASE)
        addModifiers(Modifier.PUBLIC)
        addMethod(MethodSpec.constructorBuilder().apply {
            packages.forEach { pkg ->
                val mapper = ClassName.get(pkg, APP_CLASS_NAME)
                addStatement("addMapper(new $T())", mapper)
            }
            if (hasV1CompatMapper) {
                val compatMapper = ClassName.get(
                    BindingMapperWriter.V1_COMPAT_MAPPER_PKG,
                    BindingMapperWriter.V1_COMPAT_MAPPER_NAME)
                addStatement("addMapper(new $T())", compatMapper)
            }
            featurePackages.forEach {
                addStatement("addMapper($S)", it)
            }
            if (generateTestOverride) {
                beginControlFlow("if($N != null)", overrideField).apply {
                    addStatement("addMapper($N)", overrideField)
                }.endControlFlow()
            }
        }.build())
        if (generateTestOverride) {
            addField(overrideField)
            addStaticBlock(CodeBlock.builder()
                    .beginControlFlow("try").apply {
                addStatement("$N = ($T) $T.class.getClassLoader().loadClass($S).newInstance()",
                        overrideField, BindingMapperWriterV2.DATA_BINDER_MAPPER,
                        BindingMapperWriterV2.DATA_BINDER_MAPPER,
                        TEST_OVERRIDE)
            }.nextControlFlow("catch($T ignored)", ClassName.get(Throwable::class.java))
                    .apply {
                        addStatement("$N = null", overrideField)
                    }
                    .endControlFlow()
                    .build())
        }
    }.build()!!
}
