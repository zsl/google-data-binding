# This file has been automatically generated, please do not modify directly.
load("//tools/base/bazel:bazel.bzl", "kotlin_library", "groovy_library", "kotlin_groovy_library", "fileset")

java_library(
  name = "db-compilerCommon",
  srcs = glob([
      "compilerCommon/src/main/java/**/*.java",
      "compilerCommon/src/main/xml-gen/**/*.java",
      "compilerCommon/src/main/grammar-gen/**/*.java",
    ]),
  resource_strip_prefix="tools/data-binding/db-compilerCommon.resources",
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
  visibility = ["//visibility:public"],
)

kotlin_library(
  name = "db-compiler",
  srcs = [
      "compiler/src/main/java",
      "compiler/src/main/kotlin",
    ],
  resource_strip_prefix="tools/data-binding/db-compiler.resources",
  resources = [
      "//tools/data-binding:db-compiler.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-baseLibrary",
      "//tools/data-binding:db-compilerCommon",
          "//tools/idea:lib/guava-18.0",
          "//tools/adt/idea:android/lib/commons-io-2.4",
          "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
          "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
          "//tools/base/annotations:android-annotations",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
  visibility = ["//visibility:public"],
)

java_library(
  name = "db-compiler_testlib",
  srcs = glob([
      "compiler/src/test/java/**/*.java",
    ]),
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-compiler",
          "//tools/data-binding:db-baseLibrary",
      "//tools/data-binding:db-compilerCommon",
          "//tools/idea:lib/guava-18.0",
          "//tools/adt/idea:android/lib/commons-io-2.4",
          "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
          "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
          "//tools/base/annotations:android-annotations",
      "//tools/data-binding:db-compilerCommon_testlib",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
  visibility = ["//visibility:public"],
)

java_library(
  name = "db-baseLibrary",
  srcs = glob([
      "baseLibrary/src/main/java/**/*.java",
    ]),
  resource_strip_prefix="tools/data-binding/db-baseLibrary.resources",
  resources = [
      "//tools/data-binding:db-baseLibrary.res",
    ],
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
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
   visibility = ["//visibility:public"],
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
   visibility = ["//visibility:public"],
)

java_library(
  name = "db-compilerCommon_testlib",
  srcs = glob([
      "compilerCommon/src/test/java/**/*.java",
    ]),
  deps = [
      "@local_jdk//:langtools-neverlink",
      "//tools/data-binding:db-compilerCommon",
          "//tools/idea:lib/guava-18.0",
          "//tools/adt/idea:android/lib/commons-io-2.4",
          "//tools/adt/idea:android/lib/juniversalchardet-1.0.3",
          "//tools/adt/idea:android/lib/antlr4-runtime-4.5.3",
          "//tools/base/annotations:android-annotations",
      "//tools/data-binding:db-baseLibrary",
      "//prebuilts/tools/common/m2:repository/junit/junit/4.12/junit-4.12",
      "//prebuilts/tools/common/m2:repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3",
    ],
  visibility = ["//visibility:public"],
)
