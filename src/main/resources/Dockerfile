FROM openjdk:15
MAINTAINER CoderNoOne firelight.code@gmail.com

EXPOSE 8080
WORKDIR ./usr/webflux-app
ADD ../../../target/cinema_webflux_ddd-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "--enable-preview", "-Dspring.profiles.active=docker", "-Djava.net.preferIPv4Stack=true", "app.jar"]