#!/bin/bash
set -e

#REQUIREMENTS:
## - java 11
## - maven
## - docker

#variables
NAME="spring-boot-demo"
GROUPID="cz.oxus.cvut"
DOCKER_TAG="cvut/$NAME:v1"

#generate java project
java -jar target/spring-boot-initializer-1.0-SNAPSHOT.jar "$NAME" "$GROUPID"

###build docker image and run docker image on localhost
cd destination
#build java project
mvn -Dmaven.test.skip=true clean package
#build docker image
docker build --build-arg APPLICATION_NAME="$NAME" --build-arg APPLICATION_VERSION="1.0.0-SNAPSHOT" -t "$DOCKER_TAG" .
echo "Check swagger-ui on http://localhost:8080/swagger-ui.html"
#run image on localhost:8080
docker run -p 8080:8080 "$DOCKER_TAG"