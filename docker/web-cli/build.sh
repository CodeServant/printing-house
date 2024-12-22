cd `dirname $0`/../../

./gradlew :webCli:jsBrowserDistribution

app_version_toml=`sed -n "s/^.*app\s*=\s*\x22\(\S*\)\x22.*$/\1/p" gradle/libs.versions.toml`

docker build -t web-cli:${app_version_toml} --build-arg version=$app_version_toml webCli/