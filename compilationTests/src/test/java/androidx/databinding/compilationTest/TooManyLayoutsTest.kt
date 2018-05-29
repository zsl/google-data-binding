/*
 * Copyright (C) 2018 The Android Open Source Project
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
package androidx.databinding.compilationTest

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Stress test that creates many layouts and hopes that we don't choke compiler
 */
@RunWith(JUnit4::class)
class TooManyLayoutsTest : BaseCompilationTest(true) {
    @Test
    fun tooManyLayouts() {
        prepareProject()
        (0 until 2000).forEach {
            copyResourceTo("/layout/basic_layout.xml",
                    "/app/src/main/res/layout/layout_$it.xml")
        }
        val result = runGradle("assembleDebug")
        assertEquals(result.error, 0, result.resultCode.toLong())
    }
}