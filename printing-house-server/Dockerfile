FROM ubuntu:24.04
RUN apt-get -y update
RUN apt-get install -y openjdk-21-jdk && apt-get clean
WORKDIR /opt/printing-house/server/
COPY build/libs/printing-house-server-0.0.1-SNAPSHOT.jar lib/printing-house-server.jar


ENTRYPOINT ["java", "-jar", "lib/printing-house-server.jar", "--spring.profiles.active=dev"]