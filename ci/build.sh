#!/usr/bin/env bash

set -e

timestamp=`date +%s`

export TERM=dumb
export GRADLE_OPTS="-Dorg.gradle.native=false"

cd settler-code
./gradlew -v
./gradlew assemble -PVersion=$timestamp
