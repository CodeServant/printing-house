# printing house

## [printing house server](./printing-house-server/README.md)

Server side application, that exposes REST API, for print house internal system.

To test this service do this.
- Build server and web-cli software docker image. (required java and docker)
```
bash docker/server/build.sh &&
bash docker/web-cli/build.sh
```
- run docker compose 
```
docker compose -f docker/docker-compose.yml up -d
```
It requires also populate database with relevant data to start using the software.

If you want to **run REST** server for **development** purpose you should invoke  
```gradlew :printing-house-server:bootRun --args='--spring.profiles.active=dev'```.  
To **run webCli** in continuous developer mode use this command
```gradlew :webCli:run -t```.