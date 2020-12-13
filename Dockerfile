FROM openjdk:15
MAINTAINER CoderNoOne firelight.code@gmail.com

EXPOSE 8000
WORKDIR ./usr/webflux-app
ADD ./target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "--enable-preview", "-Dspring.profiles.active=docker", "app.jar"]