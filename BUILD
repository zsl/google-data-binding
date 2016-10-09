# This file has been automatically generated, please do not modify directly.
load("//tools/base/bazel:bazel.bzl", "iml_module")

# TODO: move these to baseLibrary/, once we can use build.bazel
iml_module(
    name = "studio.baseLibrary",
    srcs = ["baseLibrary/src/main/java"],
    deps = [
        "//tools/data-binding:db-baseLibrary[test]",
        "//tools/data-binding:db-baseLibrary_0[test]",
    ],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "tools.baseLibrary",
    srcs = glob(["baseLibrary/src/main/java/**"]),
    visibility = ["//visibility:public"],
)

iml_module(
    name = "studio.compilerCommon",
    srcs = [
        "compilerCommon/src/main/java",
        "compilerCommon/src/main/xml-gen",
        "compilerCommon/src/main/grammar-gen",
    ],
    test_srcs = ["compilerCommon/src/test/java"],
    deps = [
        "//tools/data-binding:studio.baseLibrary[module]",
        "//tools/idea/.idea/libraries:Guava",
        "//tools/idea/.idea/libraries:commons-io-2.4",
        "//tools/idea/.idea/libraries:juniversalchardet-1.0.3",
        "//tools/idea/.idea/libraries:antlr4-runtime-4.5.3",
        "//tools/data-binding:db-compilerCommon[test]",
        "//tools/data-binding:db-compilerCommon_0[test]",
        "//tools/base/annotations:studio.android-annotations[module]",
    ],
    exports = [
        "//tools/idea/.idea/libraries:Guava",
        "//tools/idea/.idea/libraries:commons-io-2.4",
        "//tools/idea/.idea/libraries:juniversalchardet-1.0.3",
        "//tools/idea/.idea/libraries:antlr4-runtime-4.5.3",
        "//tools/base/annotations:studio.android-annotations",
    ],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "tools.compilerCommon",
    srcs = glob([
        "compilerCommon/src/main/java/**/*.java",
        "compilerCommon/src/main/grammar-gen/**/*.java",
        "compilerCommon/src/main/xml-gen/**/*.java",
    ]),
    visibility = ["//visibility:public"],
    deps = [
        ":tools.baseLibrary",
        "//tools/base/annotations",
        "//tools/base/third_party:com.google.guava_guava",
        "//tools/base/third_party:commons-io_commons-io",
        "//tools/base/third_party:com.googlecode.juniversalchardet_juniversalchardet",
        "//tools/base/third_party:org.antlr_antlr4",
    ],
)

java_test(
    name = "tools.compilerCommon_tests",
    srcs = glob(["compilerCommon/src/test/java/**"]),
    jvm_flags = ["-Dtest.suite.jar=tests.jar"],
    test_class = "com.android.testutils.JarTestSuite",
    deps = [
        ":tools.compilerCommon",
        "//tools/base/third_party:junit_junit",
    ],
    runtime_deps = ["//tools/base/testutils:tools.testutils"],
)

iml_module(
    name = "studio.compiler",
    srcs = [
        "compiler/src/main/java",
        "compiler/src/main/kotlin",
    ],
    test_srcs = ["compiler/src/test/java"],
    deps = [
        "//tools/data-binding:studio.baseLibrary[module]",
        "//tools/data-binding:studio.compilerCommon[module]",
        "//tools/idea/.idea/libraries:KotlinJavaRuntime",
        "//tools/data-binding:db-compiler[test]",
        "//tools/data-binding:db-compiler_0[test]",
    ],
    exports = ["//tools/data-binding:studio.baseLibrary"],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "db-baseLibrary_0",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "db-baseLibrary_1",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-compilerCommon_0",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "db-compilerCommon_1",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-compiler_0",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "db-compiler_1",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-baseLibrary",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "db-compilerCommon",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)

java_library(
    name = "db-compiler",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    visibility = ["//visibility:public"],
    tags = ["managed"],
)
