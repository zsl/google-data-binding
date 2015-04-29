/*
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.databinding.tool.writer

import android.databinding.tool.LayoutBinder

class DataBinderWriter(val pkg: String, val projectPackage: String, val className: String,
        val layoutBinders : List<LayoutBinder>, val minSdk : kotlin.Int) {
    fun write() = kcode("") {
        nl("package $pkg;")
        nl("import $projectPackage.BR;")
        nl("class $className {") {
            tab("final static int TARGET_MIN_SDK = ${minSdk};")
            nl("")
            tab("private final java.util.HashMap<String, Integer> mLayoutIds;")
            nl("")
            tab("public $className() {") {
                tab("mLayoutIds = new java.util.HashMap<String, Integer>();")
                layoutBinders.forEach {
                    tab("mLayoutIds.put(\"${it.getTag()}_0\", ${it.getModulePackage()}.R.layout.${it.getLayoutname()});")
                }
            }
            tab("}")
            nl("")
            tab("public android.databinding.ViewDataBinding getDataBinder(android.view.View view, int layoutId) {") {
                tab("switch(layoutId) {") {
                    layoutBinders.groupBy{it.getLayoutname()}.forEach {
                        val firstVal = it.value.get(0)
                        tab("case ${firstVal.getModulePackage()}.R.layout.${firstVal.getLayoutname()}:") {
                            if (it.value.size() == 1) {
                                if (firstVal.isMerge()) {
                                    tab("return new ${firstVal.getPackage()}.${firstVal.getImplementationName()}(new android.view.View[]{view});")
                                } else {
                                    tab("return ${firstVal.getPackage()}.${firstVal.getImplementationName()}.bind(view);")
                                }
                            } else {
                                // we should check the tag to decide which layout we need to inflate
                                tab("{") {
                                    tab("final Object tag = view.getTag();")
                                    tab("if(tag == null) throw new java.lang.RuntimeException(\"view must have a tag\");")
                                    it.value.forEach {
                                        tab("if (\"${it.getTag()}_0\".equals(tag)) {") {
                                            if (it.isMerge()) {
                                                tab("return new ${it.getPackage()}.${it.getImplementationName()}(new android.view.View[]{view});")
                                            } else {
                                                tab("return new ${it.getPackage()}.${it.getImplementationName()}(view);")
                                            }
                                        } tab("}")
                                    }
                                }tab("}")
                            }

                        }
                    }
                }
                tab("}")
                tab("return null;")
            }
            tab("}")

            tab("android.databinding.ViewDataBinding getDataBinder(android.view.View[] views, int layoutId) {") {
                tab("switch(layoutId) {") {
                    layoutBinders.filter{it.isMerge()}.groupBy{it.getLayoutname()}.forEach {
                        val firstVal = it.value.get(0)
                        tab("case ${firstVal.getModulePackage()}.R.layout.${firstVal.getLayoutname()}:") {
                            if (it.value.size() == 1) {
                                tab("return new ${firstVal.getPackage()}.${firstVal.getImplementationName()}(views);")
                            } else {
                                // we should check the tag to decide which layout we need to inflate
                                tab("{") {
                                    tab("final Object tag = views[0].getTag();")
                                    tab("if(tag == null) throw new java.lang.RuntimeException(\"view must have a tag\");")
                                    it.value.forEach {
                                        tab("if (\"${it.getTag()}_0\".equals(tag)) {") {
                                            tab("return new ${it.getPackage()}.${it.getImplementationName()}(views);")
                                        } tab("}")
                                    }
                                }tab("}")
                            }
                        }
                    }
                }
                tab("}")
                tab("return null;")
            }
            tab("}")

            tab("int getLayoutId(String tag) {") {
                tab("Integer id = mLayoutIds.get(tag);")
                tab("if (id == null) {") {
                    tab("return 0;")
                }
                tab("}")
                tab("return id;")
            }
            tab("}")

            tab("public int getId(String key) {") {
                tab("return BR.getId(key);")
            }
            tab("}")
        }
        nl("}")
    }.generate()
}