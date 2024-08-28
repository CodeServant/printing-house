# printing house

## [printing house server](./printing-house-server/README.md)

Server side application, that exposes REST API, for print house internal system.

To configure database use docker command
```
docker run --name printing-house-dev-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=developerDB -d mysql:9
```

If you want to **run REST** server for **development** purpose you should invoke  
```gradlew :printing-house-server:bootRun --args='--spring.profiles.active=dev'```.  
To **run webCli** in continuous developer mode use this command
```gradlew :webCli:run -t```.