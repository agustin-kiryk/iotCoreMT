FROM openjdk:11
MAINTAINER DSI
COPY target/demo-0.0.1-SNAPSHOT.jar iot-core-mt-app.jar
# Crea una carpeta para los certificados y copia los archivos dentro del contenedor
RUN mkdir /certs
COPY 8210489b5e-certificate_pem.crt /certs/
COPY 8210489b5e-private_pem.key /certs/
COPY AmazonRootCA1.pem /certs/

# Define la variable de entorno para indicar la ubicación de los certificados en el contenedor
ENV CERT_PATH certs/8210489b5e-certificate_pem.crt
ENV KEY_PATH certs/8210489b5e-private_pem.key
ENV CA_PATH certs/AmazonRootCA1.pem
ENTRYPOINT ["java", "-jar", "/iot-core-mt-app.jar"]
