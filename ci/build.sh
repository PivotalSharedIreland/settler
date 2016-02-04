#!/usr/bin/env bash

set -e

RELEASE_INFO=$PWD/release-info

timestamp=`date +%s`

export TERM=dumb
export GRADLE_OPTS="-Dorg.gradle.native=false"

echo $timestamp > $RELEASE_INFO/build-number

pushd settler-code
./gradlew -v
./gradlew assemble -PVersion=$timestamp
popd

echo "=================="
ls settler-code/backend/build/libs