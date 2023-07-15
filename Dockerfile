FROM openjdk:11-jre-slim
MAINTAINER DSI
COPY target/iotCoreMT-0.0.1-SNAPSHOT.jar iotCoreMT-app.jar
ENTRYPOINT ["java", "-jar", "/iotCoreMT-app.jar"]
