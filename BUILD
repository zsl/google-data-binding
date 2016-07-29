# This file has been automatically generated, please do not modify directly.
load("//tools/base/bazel:bazel.bzl", "kotlin_library", "groovy_library", "kotlin_groovy_library", "fileset")

java_library(
  name = "db-compilerCommon",
  srcs = glob([
      "compilerCommon/src/main/java/**/*.java",
      "compilerCommon/src/main/xml-gen/**/*.java",
      "compilerCommon/src/main/grammar-gen/**/*.java",
    ]),
  resource_strip_prefix = "tools/data-binding/db-compilerCommon.resources",
  resources = [
      "//tools/data-binding:db-compilerCommon.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-baseLibrary",
      "//tools/idea:lib/guava-18.0",
      "//tools/adt/idea:android/lib/commons-io-2.4",
      "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
      "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
      "//tools/base/annotations:android-annotations",
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

kotlin_library(
  name = "db-compiler",
  srcs = [
      "compiler/src/main/java",
      "compiler/src/main/kotlin",
    ],
  resource_strip_prefix = "tools/data-binding/db-compiler.resources",
  resources = [
      "//tools/data-binding:db-compiler.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-baseLibrary",
      "//tools/data-binding:db-compilerCommon",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
  exports = [
      "//tools/data-binding:db-baseLibrary",
    ],
  javacopts = ["-extra_checks:off"],
  visibility = ["//visibility:public"],
)

java_library(
  name = "db-compiler_testlib",
  srcs = glob([
      "compiler/src/test/java/**/*.java",
    ]),
  resource_strip_prefix = "tools/data-binding/db-compiler_testlib.resources",
  resources = [
      "//tools/data-binding:db-compiler_testlib.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-compiler",
      "//tools/data-binding:db-baseLibrary",
      "//tools/data-binding:db-compilerCommon",
      "//tools/data-binding:db-compilerCommon_testlib",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
  exports = [
      "//tools/data-binding:db-compiler",
      "//tools/data-binding:db-baseLibrary",
    ],
  javacopts = ["-extra_checks:off"],
  visibility = ["//visibility:public"],
)

java_library(
  name = "db-baseLibrary",
  srcs = glob([
      "baseLibrary/src/main/java/**/*.java",
    ]),
  resource_strip_prefix = "tools/data-binding/db-baseLibrary.resources",
  resources = [
      "//tools/data-binding:db-baseLibrary.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
  javacopts = ["-extra_checks:off"],
  visibility = ["//visibility:public"],
)

fileset(
  name = "db-compilerCommon.res",
  srcs = glob([
      "compilerCommon/src/main/java/**/*",
      "compilerCommon/src/main/xml-gen/**/*",
      "compilerCommon/src/main/grammar-gen/**/*",
    ],
    exclude = [
      "**/* *",
      "**/*.java",
      "**/*.kt",
      "**/*.groovy",
      "**/*$*",
      "**/.DS_Store",
    ]),
  mappings = {
      "compilerCommon/src/main/xml-gen": "db-compilerCommon.resources",
      "compilerCommon/src/main/grammar-gen": "db-compilerCommon.resources",
      "compilerCommon/src/main/java": "db-compilerCommon.resources",
    },
  deps = [
      "@local_jdk//:langtools-neverlink",
    ],
)

java_test(
  name = "db-compiler_tests",
  srcs = glob([
    ]),
  runtime_deps = [
      ":db-compiler_testlib",
      "//tools/base/testutils:testutils",
    ],
  jvm_flags = [
      "-Dtest.suite.jar=db-compiler_testlib.jar",
    ],
  test_class = "com.android.testutils.JarTestSuite",
  javacopts = ["-extra_checks:off"],
  visibility = ["//visibility:public"],
)

fileset(
  name = "db-compilerCommon_testlib.res",
  srcs = glob([
      "compilerCommon/src/test/java/**/*",
    ],
    exclude = [
      "**/* *",
      "**/*.java",
      "**/*.kt",
      "**/*.groovy",
      "**/*$*",
      "**/.DS_Store",
    ]),
  mappings = {
      "compilerCommon/src/test/java": "db-compilerCommon_testlib.resources",
    },
  deps = [
      "@local_jdk//:langtools-neverlink",
    ],
)

fileset(
  name = "db-compiler.res",
  srcs = glob([
      "compiler/src/main/java/**/*",
      "compiler/src/main/kotlin/**/*",
    ],
    exclude = [
      "**/* *",
      "**/*.java",
      "**/*.kt",
      "**/*.groovy",
      "**/*$*",
      "**/.DS_Store",
    ]),
  mappings = {
      "compiler/src/main/java": "db-compiler.resources",
      "compiler/src/main/kotlin": "db-compiler.resources",
    },
  deps = [
      "@local_jdk//:langtools-neverlink",
    ],
)

fileset(
  name = "db-compiler_testlib.res",
  srcs = glob([
      "compiler/src/test/java/**/*",
    ],
    exclude = [
      "**/* *",
      "**/*.java",
      "**/*.kt",
      "**/*.groovy",
      "**/*$*",
      "**/.DS_Store",
    ]),
  mappings = {
      "compiler/src/test/java": "db-compiler_testlib.resources",
    },
  deps = [
      "@local_jdk//:langtools-neverlink",
    ],
)

java_test(
  name = "db-compilerCommon_tests",
  srcs = glob([
    ]),
  runtime_deps = [
      ":db-compilerCommon_testlib",
      "//tools/base/testutils:testutils",
    ],
  jvm_flags = [
      "-Dtest.suite.jar=db-compilerCommon_testlib.jar",
    ],
  test_class = "com.android.testutils.JarTestSuite",
  javacopts = ["-extra_checks:off"],
  visibility = ["//visibility:public"],
)

fileset(
  name = "db-baseLibrary.res",
  srcs = glob([
      "baseLibrary/src/main/java/**/*",
    ],
    exclude = [
      "**/* *",
      "**/*.java",
      "**/*.kt",
      "**/*.groovy",
      "**/*$*",
      "**/.DS_Store",
    ]),
  mappings = {
      "baseLibrary/src/main/java": "db-baseLibrary.resources",
    },
  deps = [
      "@local_jdk//:langtools-neverlink",
    ],
)

java_library(
  name = "db-compilerCommon_testlib",
  srcs = glob([
      "compilerCommon/src/test/java/**/*.java",
    ]),
  resource_strip_prefix = "tools/data-binding/db-compilerCommon_testlib.resources",
  resources = [
      "//tools/data-binding:db-compilerCommon_testlib.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-compilerCommon",
      "//tools/data-binding:db-baseLibrary",
      "//tools/idea:lib/guava-18.0",
      "//tools/adt/idea:android/lib/commons-io-2.4",
      "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
      "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
      "//tools/base/annotations:android-annotations",
    ],
  exports = [
      "//tools/data-binding:db-compilerCommon",
      "//tools/idea:lib/guava-18.0",
      "//tools/adt/idea:android/lib/commons-io-2.4",
      "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
      "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
      "//tools/base/annotations:android-annotations",
    ],
  javacopts = ["-extra_checks:off"],
  visibility = ["//visibility:public"],
)
