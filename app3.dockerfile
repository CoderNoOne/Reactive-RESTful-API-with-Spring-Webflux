FROM openjdk:15
MAINTAINER CoderNoOne firelight.code@gmail.com

EXPOSE 8080
COPY mvnw /
COPY .mvn /.mvn

ENTRYPOINT /mvnw spring-boot:run -Dspring-boot.run.profiles=docker  -Dspring-boot.run.jvmArguments="--enable-preview"



