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
package android.databinding
import org.apache.commons.io.IOUtils
import java.util.jar.JarFile
import org.gradle.api.artifacts.ResolvedArtifact

class LicenseCollector {
    List<ResolvedArtifact> artifacts = new ArrayList();
    def knownLicenses = [
            [
    libraries : ["kotlin-stdlib", "kotlin-runtime"],
    licenses : ["https://raw.githubusercontent.com/JetBrains/kotlin/master/license/LICENSE.txt",
            "http://www.apache.org/licenses/LICENSE-2.0.txt"],
    notices : ["https://raw.githubusercontent.com/JetBrains/kotlin/master/license/NOTICE.txt"]
            ],
            [
    libraries : ["antlr4", "antlr4-runtime", "antlr-runtime", "antlr4-annotations"],
    licenses : ["https://raw.githubusercontent.com/antlr/antlr4/master/LICENSE.txt"]
            ],
            [
    libraries : ["java.g4"],
    licenses : ["https://raw.githubusercontent.com/antlr/antlr4/master/LICENSE.txt"]
            ],
            [
    libraries : ["ST4"],
    licenses : ["https://raw.githubusercontent.com/antlr/stringtemplate4/master/LICENSE.txt"]
            ],
            [
    libraries : ["org.abego.treelayout.core"],
    licenses: ["http://treelayout.googlecode.com/files/LICENSE.TXT"]
            ],
            [
    libraries : ["junit"],
    licenses : ["https://raw.githubusercontent.com/junit-team/junit/master/LICENSE-junit.txt"],
    notices: ["https://raw.githubusercontent.com/junit-team/junit/master/NOTICE.txt"]
            ],
            [
    libraries : ["commons-lang3", "commons-io", "commons-codec"],
    licenses : ["http://svn.apache.org/viewvc/commons/proper/lang/trunk/LICENSE.txt?revision=560660&view=co"],
    notices : ["http://svn.apache.org/viewvc/commons/proper/lang/trunk/NOTICE.txt?view=co"]
            ],
            [
    libraries : ["guava"],
    licenses : ["http://www.apache.org/licenses/LICENSE-2.0.txt"]
            ],
            [
    libraries : ["hamcrest-core"],
    licenses : ["https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE.txt"]
            ]
            ]

    Set<String> licenseLookup = new HashSet();

    LicenseCollector() {
        knownLicenses.each {
            licenseLookup.addAll(it.libraries)
        }
    }

    def skipLicenses = ['baseLibrary', 'grammarBuilder', 'xmlGrammar', 'compiler']
    public void add(ResolvedArtifact artifact) {
        artifacts.add(artifact)
    }

    public String buildNotice() {
        artifacts.each { artifact ->
            if (!skipLicenses.contains(artifact.getName())) {
                if (!licenseLookup.contains(artifact.getName())) {
                    throw new RuntimeException("Cannot find license for ${artifact.getName()} in ${artifact.getFile()}")
                }
            }
        }

        // now build the output
        StringBuilder notice = new StringBuilder();
        notice.append("List of 3rd party licenses:")
        knownLicenses.each {
            notice.append("\n-----------------------------------------------------------------------------")
            it.libraries.each {
                notice.append("\n* $it")
            }
            notice.append("\n")
            if (it.notices != null) {
                it.notices.each {
                    notice.append("\n ****** NOTICE:\n${new URL(it).getText()}")
                }
            }
            it.licenses.each {
                notice.append("\n ****** LICENSE:\n${new URL(it).getText()}")
            }
            notice.append("\n\n\n")
        }
        return notice.toString()
    }
}