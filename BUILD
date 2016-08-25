# This file has been automatically generated, please do not modify directly.
load("//tools/base/bazel:bazel.bzl", "iml_module")

iml_module(
    name = "db-baseLibrary",
    srcs = ["baseLibrary/src/main/java"],
    deps = [
        "//tools/data-binding:db-baseLibrary_0[test]",
        "//tools/data-binding:db-baseLibrary_1[test]",
    ],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
)

iml_module(
    name = "db-compilerCommon",
    srcs = [
        "compilerCommon/src/main/java",
        "compilerCommon/src/main/xml-gen",
        "compilerCommon/src/main/grammar-gen",
    ],
    test_srcs = ["compilerCommon/src/test/java"],
    deps = [
        "//tools/data-binding:db-baseLibrary[module]",
        "//tools/idea/.idea/libraries:Guava",
        "//tools/idea/.idea/libraries:commons-io-2.4",
        "//tools/idea/.idea/libraries:juniversalchardet-1.0.3",
        "//tools/idea/.idea/libraries:antlr4-runtime-4.5.3",
        "//tools/data-binding:db-compilerCommon_0[test]",
        "//tools/data-binding:db-compilerCommon_1[test]",
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
)

iml_module(
    name = "db-compiler",
    srcs = [
        "compiler/src/main/java",
        "compiler/src/main/kotlin",
    ],
    test_srcs = ["compiler/src/test/java"],
    deps = [
        "//tools/data-binding:db-baseLibrary[module]",
        "//tools/data-binding:db-compilerCommon[module]",
        "//tools/idea/.idea/libraries:KotlinJavaRuntime",
        "//tools/data-binding:db-compiler_0[test]",
        "//tools/data-binding:db-compiler_1[test]",
    ],
    exports = ["//tools/data-binding:db-baseLibrary"],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-baseLibrary_0",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-baseLibrary_1",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-compilerCommon_0",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-compilerCommon_1",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-compiler_0",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/junit/junit/4.12:jar"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "db-compiler_1",
    runtime_deps = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    exports = ["//prebuilts/tools/common/m2/repository/org/hamcrest/hamcrest-core/1.3:jar"],
    visibility = ["//visibility:public"],
)
