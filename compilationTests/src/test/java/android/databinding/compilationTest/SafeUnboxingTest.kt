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

package android.databinding.compilationTest

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SafeUnboxingTest(pair: Pair<String, Boolean>) : BaseCompilationTest(true) {
    private val expr  = pair.first
    private val shouldWarn = pair.second
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun getParams() =
            listOf(
                    "@{myInt > 0 ? `t` : `f`}" to true,
                    "@{myInt == null ? `t` : `f`}" to false,
                    "@{myInt != null ? `t` : `f`}" to false,
                    "@{myInt ?? 3}" to false,
                    "@{myInt ?? Integer.valueOf(`3`)}" to true,
                    "@{myInt ?? Integer.parseInt(`3`)}" to false,
                    "@{myInt == null ? Integer.valueOf(`3`) : myInt}" to true,
                    "@{null == myInt ? `t` : `f`}" to false,
                    "@{null != myInt ? `t` : `f`}" to false,
                    "@{null == myInt ? Integer.valueOf(`3`) : myInt}" to true
            )
    }
    @Test
    fun check() {
        prepareProject()
        copyResourceTo("/layout/safe_unbox_layout.xml",
                "/app/src/main/res/layout/main.xml",
                mapOf("EXPR" to expr))
        val result = runGradle("assembleDebug")
        assertThat(result.error, result.resultCode, `is`(0))

        assertThat(result.error, result.bindingWarnings.any {
            it.contains(" is a boxed field but needs to be un-boxed to execute")
        }, `is`(shouldWarn))
    }
}