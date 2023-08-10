FROM openjdk:17
MAINTAINER CoderNoOne firelight.code@gmail.com

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*","-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005", "-Dspring.profiles.active=docker", "-Djava.net.preferIPv4Stack=true", "com.app.CinemaApplication"]

#Za kazdym razem, kiedy ladujemy FAT JAR musimy od nowa
#wgrywac wszystko co w nim jest. A co w nim jest?
#DEPENDENCIES oraz CLASSES ktore pochodza z kompilacji,
#zamiast calego FAT JAR ladowac
#tylko CLASSES. To pozwoli do cache raz zaladowac DEPENDENCIES
#ktore uzywamy i w przyszlosci podmieniac tylko male CLASSES
#bez potrzeby ponownego ladowania DEPENDENCIES.
#Czyli uzyskamy nastepujaca strukture:
#
#------------------------------------------------
#CLASSES
#------------------------------------------------
#DEPENDENCIES
#------------------------------------------------
#JDK