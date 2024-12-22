#!/bin/bash

# change to project root
cd `dirname $0`/../../

./gradlew :printing-house-server:bootJar

app_version_toml=`sed -n "s/^.*app\s*=\s*\x22\(\S*\)\x22.*$/\1/p" gradle/libs.versions.toml`

docker build -t printing-house-server:${app_version_toml} --build-arg version=$app_version_toml printing-house-server/

