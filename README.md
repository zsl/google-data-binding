## Setting up
run the init.sh script in {src}/tools/data-binding. This will make the extensions and integration
tests runnable directly.

`cd {src}/tools/data-binding && ./init.sh`

To match the sdk used by android gradle plugin, this script sets the local properties to use the
checked in sdk. This might conflict with your local sdk or Android Studio setup. Unfortunately,
Android Studio does not support multiple SDKs so you need to live with that. (and your local sdk
might just work fine if tools did not upgrade to an unreleased
version. YMMV)

## Building Artifacts
### Compile Time Artifacts
They are compiled as part of the Android Gradle Plugin when they are shipped.
Under {src}/tools, you can run tasks for

`./gradlew :dB:<TASK_NAME>`

It also works independently, so you can just run

`cd {src}/data-binding && ./gradlew :dB:comp:test`

It also compiles with BAZEL :). It will take ages when you run it for the first time, go for a
coffee. Then it will be faster and more reliable than gradle.

`bazel build //tools/data-binding/...`

### Runtime Artifacts (extensions)
This project is compiled using android gradle plugin or bazel
to compile from gradle (under {src}/tools)

`./gradlew :dB:buildDataBindingRuntimeArtifacts`

to compile from bazel run

`bazel build //tools/data-binding:runtimeLibraries`

You can also compile them from command line if you've run init.sh
you must first compile the android gradle plugin:

```
cd {src}/tools && ./gradlew :pL
cd {src}/tools/data-binding && ./gradlew build
```

## Running in the IDE
### Compile Time Code
The main project still has proper gradle configuration so you can open it in Intellij

### Runtime Libraries (a.k.a extensions)
First, compile the local gradle plugin and also make sure you've run init.gradle

`cd {src}/tools && ./gradlew :pL`

The run the init script:

`cd {src}/tools/data-binding && ./init.sh`

Now you can open extensions in Android Studio.

## Running Android Gradle Plugin Data Binding Tests
Some of data binding tests are only in AGP. To run them:

`gw :base:build-system:integration-test:application:te -D:base:build-system:integration-test:application:test.single=DataBinding\*`

### Running Integration Tests
These are run by gradle build.

`gw :base:build-system:integration-test:application:cIT -D:base:build-system:integration-test:application:connectedIntegrationTest.single=DataBinding\*`

We also compile them in bazel builds:

`bazel test //tools/base/build-system/integration-test/application:tests --test_filter=DataBinding\* --test_output=errors --action_env="GTEST_COLOR=1"`

If you did run `./init.sh`, you can open integration tests in Android Studio.

### Making Build File Changes
There are multiple ways data binding is compiled so build file changes are a bit tricky.

If you add a new dependency, you should update:
   {src}/tools/data-binding/BUILD.bazel
   compiler/db-compiler.iml and compilerCommon/db-compilerCommon.iml

Manually editing them and then running bazel to test is the most reliable approach. If you break it,
presubmit will catch. Bazel uses these iml files while compiling. You may need to modify the .idea
folder inside {src}/tools if your dependency does not already exist for some other project.

After changing the iml files, you should run `bazel run //tools/base/bazel:iml_to_build` to
re-generate the related bazel files. (if you forget, presubmit will probably catch it)

If you add a new integration test app, update
{src}/tools/base/build-system/integration-test/src/test/java/com/android/build/gradle/integration/databinding/DataBindingIntegrationTestAppsTest.java to include it.


## Misc

### working on compiler
If you are working on compiler but testing via integration tests, run:
`./gradlew :publishAndroidGradleLocal //(in tools/base)`
then run your integration test.

### all gradle tests at once
 gw :base:build-system:integration-test:databinding:test :base:build-system:integration-test:application:cIT -D:base:build-system:integration-test:application:connectedIntegrationTest.single=DataBinding\*


### generating online docs for runtime libs

`cd extensions && gw  :generateDocs -Pandroid.injected.invoked.from.ide=true --info -Ponline=true -PincludeDoclava`

// remove online parameter to get offline docs
// we pass invoked from ide to enable default setup