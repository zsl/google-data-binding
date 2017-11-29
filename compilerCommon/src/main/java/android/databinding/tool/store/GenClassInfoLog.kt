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

package android.databinding.tool.store

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File

/**
 * includes the mapping from key to the generated binding class and its variables.
 * e.g: generic_view to android.databinding.testapp.databinding.GenericViewBinding
 */
data class GenClassInfoLog(
        @SerializedName("mappings")
        private val mappings: MutableMap<String, GenClass> = mutableMapOf()) {

    fun mappings(): Map<String, GenClass> = mappings

    companion object {
        private val GSON = GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().create()

        @JvmStatic
        fun fromFile(file: File): GenClassInfoLog {
            if (!file.exists()) {
                return GenClassInfoLog()
            }
            return file.reader(Charsets.UTF_16).use {
                GSON.fromJson(it, GenClassInfoLog::class.java)
            }
        }
    }

    fun addAll(other: GenClassInfoLog) {
        other.mappings.forEach {
            addMapping(it.key, it.value)
        }
    }

    fun addMapping(infoFileName: String, klass: GenClass) {
        mappings[infoFileName] = klass
    }

    fun diff(other : GenClassInfoLog) : Set<String> {
        // find diffs w/ the other one.
        val diff = mutableSetOf<String>()
        other.mappings.forEach {
            if (mappings[it.key] == null || mappings[it.key] != it.value) {
                diff.add(it.key)
            }
        }
        mappings.forEach {
            if (other.mappings[it.key] == null || other.mappings[it.key] != it.value) {
                diff.add(it.key)
            }
        }
        return diff
    }

    fun serialize(file: File) {
        if (file.exists()) {
            file.delete()
        }
        file.writer(Charsets.UTF_16).use {
            GSON.toJson(this, it)
        }
    }

    /**
     * holds the signature for a class. We only care about the class name and its variables.
     */
    data class GenClass(
            @SerializedName("name")
            val name: String,
            @SerializedName("variables") //  var name -> type
            val variables: Map<String, String>
    )
}
