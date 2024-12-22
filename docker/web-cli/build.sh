cd `dirname $0`/../../

./gradlew :webCli:jsBrowserDistribution

source ./docker/app_version.sh

docker build -t web-cli:${app_version_toml} --build-arg version=$app_version_toml webCli/