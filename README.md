## Minesweeper Rest Api Game
Minesweeper with SpringBoot

The goal of this project is to provide a REST-API to play Minesweeper Game.
You can find all the endpoints provided in the following Swagger Link: https://peaceful-reef-33790.herokuapp.com/swagger-ui.html#/mine-sweeper-controller

### Stack
This is the Minesweeper backend game developed with the following stack technology:

* Spring Boot
* MySql
* Gradle
* JPA
* Swagger
* Testng
* SLF4J - Simple Logging Facade for Java
* Lombok

In order to deploy the application I used Heroku (https://dashboard.heroku.com)

### How to use the REST-API
One way that you can do to test all the api calls endpoints is just go to https://peaceful-reef-33790.herokuapp.com/swagger-ui.html#/mine-sweeper-controller and choose the endpoint that you wanna test. You will find there the JSON file necessary to complete and then just hit the endoint and check which response you've got. This is one of the awesome thinks that Swagger give us!!

### TODO:

Improve the error Handling with HandlerExceptionResolver or the @ExceptionHandler annotation.

Added more unit tests.

Added a tool to generate database and future migrations.
