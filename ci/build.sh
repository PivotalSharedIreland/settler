#!/usr/bin/env bash
set -e

export TERM=dumb
export GRADLE_OPTS="-Dorg.gradle.native=false"

cd settler-code
./gradlew -v
./gradlew test