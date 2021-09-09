# Spring Boot App
## Build project
```
cd app/
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

## REST API  
- `GET https:/localhost:8080/user/{username}`
**This will fetch user's details by username** 
- `POST https://localhost:8080/user`
    ```json
    {
        "username":"john",
        "birthDate":"2000-10-12",
        "countryOfResidence":"France",
        "phoneNumber":"+122342342343",
        "gender":"male"
    }
    ```
    **This will insert that user into DB**