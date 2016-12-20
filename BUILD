load("//tools/base/bazel:bazel.bzl", "iml_module", "merged_properties")
load("//tools/base/bazel:maven.bzl", "maven_java_library", "maven_pom")

# TODO: move these to baseLibrary/, once we can use build.bazel
iml_module(
    name = "studio.baseLibrary",
    srcs = ["baseLibrary/src/main/java"],
    tags = ["managed"],
    visibility = ["//visibility:public"],
    # do not sort: must match IML order
    deps = [
        "//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar[test]",
        "//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar[test]",
    ],
)

maven_java_library(
    name = "tools.baseLibrary",
    srcs = glob(["baseLibrary/src/main/java/**"]),
    pom = ":baseLibrary.pom",
    visibility = ["//visibility:public"],
)

maven_pom(
    name = "baseLibrary.pom",
    artifact = "baseLibrary",
    group = "com.android.databinding",
    source = "//tools/buildSrc/base:build_version",
)

iml_module(
    name = "studio.compilerCommon",
    # do not sort: must match IML order
    srcs = [
        "compilerCommon/src/main/java",
        "compilerCommon/src/main/xml-gen",
        "compilerCommon/src/main/grammar-gen",
    ],
    tags = ["managed"],
    test_srcs = ["compilerCommon/src/test/java"],
    visibility = ["//visibility:public"],
    # do not sort: must match IML order
    exports = [
        "//tools/idea/.idea/libraries:Guava",
        "//tools/idea/.idea/libraries:commons-io-2.4",
        "//tools/adt/idea/android/lib:juniversalchardet-1.0.3",
        "//tools/idea/.idea/libraries:antlr4-runtime-4.5.3",
        "//tools/base/annotations:studio.android-annotations",
    ],
    # do not sort: must match IML order
    deps = [
        "//tools/data-binding:studio.baseLibrary[module]",
        "//tools/idea/.idea/libraries:Guava",
        "//tools/idea/.idea/libraries:commons-io-2.4",
        "//tools/adt/idea/android/lib:juniversalchardet-1.0.3",
        "//tools/idea/.idea/libraries:antlr4-runtime-4.5.3",
        "//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar[test]",
        "//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar[test]",
        "//tools/base/annotations:studio.android-annotations[module]",
    ],
)

merged_properties(
    name = "data_binding_version_info",
    srcs = [
        "//tools/buildSrc/base:version.properties",
        "//tools/data-binding:databinding.properties",
    ],
    mappings = [
        "extensionsVersion:extensions",
        "buildVersion:compiler",
        "buildVersion:compilerCommon",
        "buildVersion:baseLibrary",
    ],
)

maven_java_library(
    name = "tools.compilerCommon",
    srcs = glob([
        "compilerCommon/src/main/java/**/*.java",
        "compilerCommon/src/main/grammar-gen/**/*.java",
        "compilerCommon/src/main/xml-gen/**/*.java",
    ]),
    pom = ":compilerCommon.pom",
    resource_strip_prefix = "tools/data-binding",
    resources = [":data_binding_version_info"],
    visibility = ["//visibility:public"],
    deps = [
        ":tools.baseLibrary",
        "//tools/base/annotations",
        "//tools/base/third_party:com.google.guava_guava",
        "//tools/base/third_party:com.googlecode.juniversalchardet_juniversalchardet",
        "//tools/base/third_party:commons-io_commons-io",
        "//tools/base/third_party:org.antlr_antlr4",
    ],
)

maven_pom(
    name = "compilerCommon.pom",
    artifact = "compilerCommon",
    group = "com.android.databinding",
    source = "//tools/buildSrc/base:build_version",
)

java_test(
    name = "tools.compilerCommon_tests",
    srcs = glob(["compilerCommon/src/test/java/**"]),
    jvm_flags = ["-Dtest.suite.jar=tests.jar"],
    test_class = "com.android.testutils.JarTestSuite",
    runtime_deps = ["//tools/base/testutils:tools.testutils"],
    deps = [
        ":tools.compilerCommon",
        "//tools/base/third_party:junit_junit",
    ],
)

iml_module(
    name = "studio.compiler",
    # do not sort: must match IML order
    srcs = [
        "compiler/src/main/java",
        "compiler/src/main/kotlin",
    ],
    resources = ["compiler/src/main/resources"],
    tags = ["managed"],
    test_data = [
        "//prebuilts/studio/sdk:platforms/android-24",
        "//prebuilts/studio/sdk:platform-tools",
    ],
    test_srcs = ["compiler/src/test/java"],
    visibility = ["//visibility:public"],
    exports = ["//tools/data-binding:studio.baseLibrary"],
    # do not sort: must match IML order
    deps = [
        "//tools/data-binding:studio.baseLibrary[module]",
        "//tools/data-binding:studio.compilerCommon[module]",
        "//tools/idea/.idea/libraries:KotlinJavaRuntime",
        "//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar[test]",
        "//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar[test]",
    ],
)
