# Docker Build Stage
FROM maven:3-jdk-8-alpine AS build


# Copy folder in docker
WORKDIR /opt/app

COPY ./ /opt/app