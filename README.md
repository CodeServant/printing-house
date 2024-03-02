# printing house

## [printing house server](./printing-house-server/README.md)

Server side application, that exposes REST API, for print house internal system.

To configure database use docker command
```
docker run --name printing-house-dev-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=developerDB -d mysql:8
```

If you want to **run REST** server for **development** purpose you should invoke  
```gradlew :printing-house-server:bootRun --args='--spring.profiles.active=dev'```  
remember to configure **environmental variables** for the database and the **database itself**.