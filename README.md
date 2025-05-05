# printing house

## [printing house server](./printing-house-server/README.md)

Server side application, that exposes REST API, for print house internal system.

To test this service do this.
- Build server and web-cli software docker image. (required java and docker)
```bash
bash docker/server/build.sh &&
bash docker/web-cli/build.sh
```
- run docker compose 
```bash
docker compose -f docker/docker-compose.yml up -d
```
It requires also populating the database with relevant data to start using the software.

To drop docker containers, write
```bash
docker compose -f docker/docker-compose.yml down -v
```


If you want to **run REST** server for **development** purpose you should invoke.  
```gradlew :printing-house-server:bootRun --args='--spring.profiles.active=dev'```   
After this you will be able to go to [swagger api documentation](http://localhost:8080/swagger-ui/index.html).   
To **run webCli** in continuous developer mode use this command
```gradlew :webCli:run -t```.