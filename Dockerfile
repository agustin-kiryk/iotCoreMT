FROM openjdk:11
MAINTAINER DSI
COPY target/demo-0.0.1-SNAPSHOT.jar iot-core-mt-app.jar
ENTRYPOINT ["java", "-jar", "/iot-core-mt-app.jar"]
