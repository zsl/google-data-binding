#!/bin/bash

readonly script_dir="$(dirname $0)"
readonly dist_dir="$1"

(cd "${script_dir}"/.. && ./gradlew :publishLocal)
(cd "${script_dir}" && ./gradlew :dataBinding:compilationTests:test)

if [[ -d "${dist_dir}" ]]; then
  # on AB/ATP, follow conventions to use gtest-testlog-forwarding
  mkdir "${dist_dir}"/gtest
  cp -av "${script_dir}"/compilationTests/build/test-results/test/*.xml "${dist_dir}"/gtest
fi
