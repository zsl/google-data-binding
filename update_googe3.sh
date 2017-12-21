#!/bin/bash
# this script uploads binaries to google3. run it only after you've built the library
# sample invocation ./update_googe3.sh 3.1.0-dev /google/src/cloud/yboyar/data-binding-3-1-dev/ v3_1_0
# the script assumes you have a virtual machine whose address is in VM environment variable
VERSION=$1 # 3.1.0-dev
REMOTE_ROOT=$2 # e.g. /google/src/cloud/yboyar/data-binding-3-1-dev/
REMOTE_VERSION=$3 # e.g. v3_1_0
if [ ! -e 'integration-tests' ] || [ ! -e 'extensions' ]; then
  echo "must run this under {src}/tools/data-binding"
  exit 1
fi

echo "copying version $VERSION to $REMOTE_ROOT under version $REMOTE_VERSION"
TMP_JAR_DIR='tmp_jar'
TMP_LIB_DIR='tmp_lib'
CUR_DIR=$PWD;
REPO_DIR="$CUR_DIR/../../out/repo"

create_target_dir() {
  echo "creating target dir $1"
  rm -rf $1
  mkdir $1
}

copy_jar() {
  TARGET=$1
  ARTIFACT_NAME=$2
  NEW_NAME=$3
  cp "$REPO_DIR/com/android/databinding/$ARTIFACT_NAME/$VERSION/$ARTIFACT_NAME-$VERSION.jar" "$TARGET/$NEW_NAME.jar"
  cp "$REPO_DIR/com/android/databinding/$ARTIFACT_NAME/$VERSION/$ARTIFACT_NAME-$VERSION-sources.jar" "$TARGET/$NEW_NAME-src.jar"
}

copy_aar() {
  TARGET=$1
  ARTIFACT_NAME=$2
  NEW_NAME=$3
  cp "$REPO_DIR/com/android/databinding/$ARTIFACT_NAME/$VERSION/$ARTIFACT_NAME-$VERSION.aar" "$TARGET/$NEW_NAME.aar"
  cp "$REPO_DIR/com/android/databinding/$ARTIFACT_NAME/$VERSION/$ARTIFACT_NAME-$VERSION-sources.jar" "$TARGET/$NEW_NAME-src.jar"
}


# copy jars
create_target_dir $TMP_JAR_DIR
copy_jar $TMP_JAR_DIR "compiler" "compiler"
copy_jar $TMP_JAR_DIR "compilerCommon" "compiler_common"
copy_jar $TMP_JAR_DIR "baseLibrary" "base"

./gradlew :dB:exec:jar
cp "exec/build/libs/exec-$VERSION.jar" "$TMP_JAR_DIR/exec.jar"
cp "exec/build/libs/exec-$VERSION-sources.jar" "$TMP_JAR_DIR/exec-src.jar"

scp $TMP_JAR_DIR/* "$VM:$REMOTE_ROOT/google3/third_party/java/android_databinding/$REMOTE_VERSION/."


# copy library
create_target_dir $TMP_LIB_DIR
copy_aar $TMP_LIB_DIR "library" "library"
cd $TMP_LIB_DIR
unzip library.aar
mv classes.jar "databinding-library.jar"
scp -r databinding-library.jar AndroidManifest.xml res proguard.txt $VM:$REMOTE_ROOT/google3/third_party/java/android/android_sdk_linux/extras/android/compatibility/databinding/library/$REMOTE_VERSION/.
cd $CUR_DIR

# copy baseAdapters source
scp -r extensions/baseAdapters/src $VM:$REMOTE_ROOT/google3/third_party/java/android/android_sdk_linux/extras/android/compatibility/databinding/adapters/$REMOTE_VERSION/.
