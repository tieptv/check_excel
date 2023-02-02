# Docker Build Stage
FROM maven:3-jdk-8-alpine AS build


# Copy folder in docker
WORKDIR /opt/app

COPY ./ /opt/app
COPY pom.xml ./pom.xml
RUN mvn clean install -DskipTests


# Run spring boot in Docker
FROM openjdk:8-jdk-alpine

ENV PORT 8081
EXPOSE $PORT

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","/opt/app/target/app.jar"]