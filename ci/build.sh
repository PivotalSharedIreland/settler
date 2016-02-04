#!/usr/bin/env bash

set -e

RELEASE_DIR=$PWD/release

timestamp=`date +%s`

export TERM=dumb
export GRADLE_OPTS="-Dorg.gradle.native=false"

echo $timestamp > $RELEASE_DIR/build-number

pushd settler-code
./gradlew -v
./gradlew assemble -PVersion=$timestamp
popd

cp settler-code/backend/build/libs/settler*.jar $RELEASE_DIR