# Spring boot initializer

## Prerequisities
 - Java 11
 - Apache Maven
 - Docker

## Compile project
```
mvn clean package
```
## Edit script variables
Use your favourite text editor and change variables inside gen-build-run.sh
## Run project generator
```
sh gen-build-run.sh
```
This command will:
 - generate new project from template inside directory 'destination'
 - compile project using maven
 - build docker image
 - start docker image on localhost
 - swagger is accessible with your browser on URL http://localhost:8080/swagger-ui.html

