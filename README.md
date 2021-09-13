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

##### If you have encounter this error (Could not start process: < EOF >) while starting the app, 
##### you have to kill the mongo process.It is due to a built-in issue in embedded mongo library  
##### Linux
```
sudo kill -9 $(lsof -i :27017 | awk '{print $2}' | grep -v "PID")
```
##### Windows
```
To get the process id on which the port number is running: 
netstat -ano | findstr :27017   
To kill the process id
taskkill /PID processID /F 
```