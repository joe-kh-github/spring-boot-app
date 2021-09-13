# Spring Boot App
## Build project
```
cd sprint-boot-app/
mvn clean package
```
## Run the app
```
java -jar app-0.0.1-SNAPSHOT.jar
```
## Run the app with different language (by default it is in English)
```
java -Duser.language=fr -jar app-0.0.1-SNAPSHOT.jar
```

## REST Api Documentation (Swagger)
##### `http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/`