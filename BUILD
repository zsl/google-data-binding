# This file has been automatically generated, please do not modify directly.
load("//tools/base/bazel:bazel.bzl", "iml_module")

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
        "//tools/idea:lib/guava-18.0",
        "//tools/adt/idea:android/lib/commons-io-2.4",
        "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
        "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
        "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12[test]",
        "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3[test]",
        "//tools/base/annotations:android-annotations[module]",
    ],
    exports = [
        "//tools/idea:lib/guava-18.0",
        "//tools/adt/idea:android/lib/commons-io-2.4",
        "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
        "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
        "//tools/base/annotations:android-annotations",
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
        "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12[test]",
        "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3[test]",
    ],
    exports = ["//tools/data-binding:db-baseLibrary"],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
)

iml_module(
    name = "db-baseLibrary",
    srcs = ["baseLibrary/src/main/java"],
    deps = [
        "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12[test]",
        "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3[test]",
    ],
    javacopts = ["-extra_checks:off"],
    visibility = ["//visibility:public"],
)
