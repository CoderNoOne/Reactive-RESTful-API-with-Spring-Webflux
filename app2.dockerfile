FROM openjdk:15
MAINTAINER CoderNoOne firelight.code@gmail.com

EXPOSE 8080

ARG USER_HOME_DIR="/root"
ARG MAVEN_VERSION=3.3.9
RUN mkdir -p /usr/share/maven && \
curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
ENV MAVEN_HOME /usr/share/maven
ENTRYPOINT ["/usr/bin/mvn"]

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY pom.xml /usr/src/app
RUN mvn -T 1C install && rm -rf target

COPY src /usr/src/app/src

