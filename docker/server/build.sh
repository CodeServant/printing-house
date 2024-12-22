#!/bin/bash

# change to project root
cd `dirname $0`/../../

./gradlew :printing-house-server:bootJar

source ./docker/app_version.sh

docker build -t printing-house-server:${app_version_toml} --build-arg version=$app_version_toml printing-house-server/

