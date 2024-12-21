# printing house

## [printing house server](./printing-house-server/README.md)

Server side application, that exposes REST API, for print house internal system.

To test this service do this.
- Build server software docker image.
```
bash docker/server/build.sh
```
- run docker compose 
```
docker compose -f docker/docker-compose.yml up -d
```

If you want to **run REST** server for **development** purpose you should invoke  
```gradlew :printing-house-server:bootRun --args='--spring.profiles.active=dev'```.  
To **run webCli** in continuous developer mode use this command
```gradlew :webCli:run -t```.