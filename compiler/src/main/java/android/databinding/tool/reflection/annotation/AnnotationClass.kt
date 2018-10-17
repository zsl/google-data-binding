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
package android.databinding.tool.reflection.annotation

import android.databinding.tool.reflection.ModelAnalyzer
import android.databinding.tool.reflection.ModelClass
import android.databinding.tool.reflection.ModelField
import android.databinding.tool.reflection.ModelMethod
import android.databinding.tool.reflection.TypeUtil
import android.databinding.tool.util.L

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

import java.util.ArrayList

import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * This is the implementation of ModelClass for the annotation
 * processor. It relies on AnnotationAnalyzer.
 */
class AnnotationClass(
        @JvmField
        val typeMirror: TypeMirror
) : ModelClass() {

    private val typeUtils: Types
        get() = AnnotationAnalyzer.get().mProcessingEnv.typeUtils

    private val elementUtils: Elements
        get() = AnnotationAnalyzer.get().mProcessingEnv.elementUtils

    override fun toJavaCode(): String {
        return if (isIncomplete) {
            canonicalName
        } else {
            AnnotationTypeUtil.getInstance().toJava(typeMirror)
        }
    }

    override fun getComponentType(): AnnotationClass? {
        val component: TypeMirror?
        when {
            isArray -> component = (typeMirror as ArrayType).componentType
            isList -> {
                for (method in getMethods("get", 1)) {
                    val parameter = method.parameterTypes[0]
                    if (parameter.isInt || parameter.isLong) {
                        val parameters = ArrayList<ModelClass>(1)
                        parameters.add(parameter)
                        return method.getReturnType(parameters) as AnnotationClass
                    }
                }
                // no "get" call found!
                return null
            }
            else -> {
                val mapClass = ModelAnalyzer.getInstance().mapType as AnnotationClass?
                val mapType = findInterface(mapClass!!.typeMirror) ?: return null
                component = mapType.typeArguments[1]
            }
        }

        return AnnotationClass(component)
    }

    private fun findInterface(interfaceType: TypeMirror): DeclaredType? {
        val typeUtil = typeUtils
        var foundInterface: TypeMirror? = null
        if (typeUtil.isSameType(interfaceType, typeUtil.erasure(typeMirror))) {
            foundInterface = typeMirror
        } else {
            val toCheck = ArrayList<TypeMirror>()
            toCheck.add(typeMirror)
            while (!toCheck.isEmpty()) {
                val typeMirror = toCheck.removeAt(0)
                if (typeUtil.isSameType(interfaceType, typeUtil.erasure(typeMirror))) {
                    foundInterface = typeMirror
                    break
                } else {
                    toCheck.addAll(typeUtil.directSupertypes(typeMirror))
                }
            }
            if (foundInterface == null) {
                L.e("Detected " + interfaceType + " type for " + typeMirror +
                        ", but not able to find the implemented interface.")
                return null
            }
        }
        if (foundInterface.kind != TypeKind.DECLARED) {
            L.e("Found " + interfaceType + " type for " + typeMirror +
                    ", but it isn't a declared type: " + foundInterface)
            return null
        }
        return foundInterface as DeclaredType?
    }

    override fun isNullable(): Boolean {
        return when (typeMirror.kind) {
            TypeKind.ARRAY, TypeKind.DECLARED, TypeKind.NULL -> true
            else -> false
        }
    }

    override fun isPrimitive(): Boolean {
        return when (typeMirror.kind) {
            TypeKind.BOOLEAN, TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT,
            TypeKind.LONG, TypeKind.CHAR, TypeKind.FLOAT, TypeKind.DOUBLE -> true
            else -> false
        }
    }

    override fun isArray() = typeMirror.kind == TypeKind.ARRAY

    override fun isBoolean()= typeMirror.kind == TypeKind.BOOLEAN

    override fun isChar() = typeMirror.kind == TypeKind.CHAR

    override fun isByte() = typeMirror.kind == TypeKind.BYTE

    override fun isShort() = typeMirror.kind == TypeKind.SHORT

    override fun isInt() = typeMirror.kind == TypeKind.INT

    override fun isLong() = typeMirror.kind == TypeKind.LONG

    override fun isFloat() = typeMirror.kind == TypeKind.FLOAT

    override fun isDouble() = typeMirror.kind == TypeKind.DOUBLE

    override fun isTypeVar() = typeMirror.kind == TypeKind.TYPEVAR

    override fun isWildcard() = typeMirror.kind == TypeKind.WILDCARD

    override fun isInterface() = typeMirror.kind == TypeKind.DECLARED &&
            (typeMirror as DeclaredType).asElement().kind == ElementKind.INTERFACE

    override fun isVoid() = typeMirror.kind == TypeKind.VOID

    override fun isGeneric(): Boolean {
        var isGeneric = false
        if (typeMirror.kind == TypeKind.DECLARED) {
            val declaredType = typeMirror as DeclaredType
            val typeArguments = declaredType.typeArguments
            isGeneric = typeArguments != null && !typeArguments.isEmpty()
        }
        return isGeneric
    }

    override fun getMinApi(): Int {
        if (typeMirror.kind == TypeKind.DECLARED) {
            val declaredType = typeMirror as DeclaredType
            val annotations = elementUtils.getAllAnnotationMirrors(declaredType.asElement())

            val targetApi = elementUtils.getTypeElement("android.annotation.TargetApi")
            val targetApiType = targetApi.asType()
            val typeUtils = typeUtils
            for (annotation in annotations) {
                if (typeUtils.isAssignable(annotation.annotationType, targetApiType)) {
                    for (value in annotation.elementValues.values) {
                        return value.value as Int
                    }
                }
            }
        }
        return super.getMinApi()
    }

    override fun getTypeArguments(): List<ModelClass>? {
        var types: MutableList<ModelClass>? = null
        if (typeMirror.kind == TypeKind.DECLARED) {
            val declaredType = typeMirror as DeclaredType
            val typeArguments = declaredType.typeArguments
            if (typeArguments != null && !typeArguments.isEmpty()) {
                types = ArrayList()
                for (typeMirror in typeArguments) {
                    types.add(AnnotationClass(typeMirror))
                }
            }
        }
        return types
    }

    override fun unbox(): AnnotationClass {
        if (!isNullable) {
            return this
        }
        return try {
            AnnotationClass(typeUtils.unboxedType(typeMirror))
        } catch (e: IllegalArgumentException) {
            // I'm being lazy. This is much easier than checking every type.
            this
        }

    }

    override fun box(): AnnotationClass {
        return if (!isPrimitive) {
            this
        } else {
            AnnotationClass(typeUtils.boxedClass(typeMirror as PrimitiveType).asType())
        }
    }

    override fun isAssignableFrom(that: ModelClass?): Boolean {
        var other: ModelClass? = that
        while (other != null && other !is AnnotationClass) {
            other = other.superclass
        }
        if (other == null) {
            return false
        }
        if (equals(other)) {
            return true
        }
        val thatAnnotationClass = other as? AnnotationClass ?: return false
        return typeUtils.isAssignable(thatAnnotationClass.typeMirror, this.typeMirror)
    }

    public override fun getDeclaredMethods(): Array<ModelMethod> {
        return if (typeMirror.kind == TypeKind.DECLARED) {
            val declaredType = typeMirror as DeclaredType
            val elementUtils = elementUtils
            val typeElement = declaredType.asElement() as TypeElement
            val members = elementUtils.getAllMembers(typeElement)
            val methods = ElementFilter.methodsIn(members)
            Array(methods.size) {
                AnnotationMethod(declaredType, methods[it])
            }
        } else {
            emptyArray()
        }
    }

    override fun getSuperclass(): AnnotationClass? {
        if (typeMirror.kind == TypeKind.DECLARED) {
            val declaredType = typeMirror as DeclaredType
            val typeElement = declaredType.asElement() as TypeElement
            val superClass = typeElement.superclass
            if (superClass.kind == TypeKind.DECLARED) {
                return AnnotationClass(superClass)
            }
        }
        return null
    }

    override fun getCanonicalName(): String {
        return AnnotationTypeUtil.getInstance().toJava(typeUtils.erasure(typeMirror))
    }

    override fun erasure(): ModelClass {
        val erasure = typeUtils.erasure(typeMirror)
        return if (erasure === typeMirror) {
            this
        } else {
            AnnotationClass(erasure)
        }
    }

    override fun getJniDescription(): String {
        return TypeUtil.getInstance().getDescription(this)
    }

    override fun getDeclaredFields(): Array<ModelField> {
        val declaredFields: Array<ModelField>
        declaredFields = if (typeMirror.kind == TypeKind.DECLARED) {
            val declaredType = typeMirror as DeclaredType
            val elementUtils = elementUtils
            val typeElement = declaredType.asElement() as TypeElement
            val members = elementUtils.getAllMembers(typeElement)
            val fields = ElementFilter.fieldsIn(members)
            Array(fields.size) {
                AnnotationField(declaredType, fields[it])
            }
        } else {
            emptyArray()
        }
        return declaredFields
    }

    override fun toString(): String {
        return AnnotationTypeUtil.getInstance().toJava(typeMirror)
    }

    override fun getTypeName(): TypeName {
        return ClassName.get(typeMirror)
    }

    override fun hashCode(): Int {
        return AnnotationTypeUtil.getInstance().toJava(typeMirror).hashCode()
    }

    @Suppress("RedundantOverride")
    override fun equals(other: Any?): Boolean {
        // intentional delegation to super which implements this in data binding generic way.
        return super.equals(other)
    }
}
