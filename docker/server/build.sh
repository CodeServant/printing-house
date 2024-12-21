#!/bin/bash

cd `dirname $0`/../../

./gradlew :printing-house-server:bootJar
docker build -t printing-house-server:0.0.1-SNAPSHOT printing-house-server/