package com.madreTierra.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.TransactionEntity;
import com.madreTierra.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.CrtResource;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.http.HttpProxyOptions;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
@Component
public class MqttClientManager {

    @Autowired
    MachineRepository machineRepository;
    static String certPath = "8210489b5e-certificate_pem.crt";
    static String keyPath = "8210489b5e-private_pem.key";
    static String caPath = "src/main/resources/AmazonRootCA1.pem";
    static String clientId = "230517_11";
    static String endpoint = "a2i1cbvebks9le-ats.iot.us-west-1.amazonaws.com";
    static int port = Integer.parseInt(("8883"));
    static String proxyHost = String.valueOf(Integer.parseInt("0"));
    static int proxyPort = Integer.parseInt("0");
   // static String topic = "dispensador/informacion/230517_1";
    static String message = "mensaje test";

    public void call() {
        MqttClientConnectionEvents callbacks = new MqttClientConnectionEvents() {
            @Override
            public void onConnectionInterrupted(int errorCode) {
                if (errorCode != 0) {
                    System.out.println("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode));
                }
            }

            @Override
            public void onConnectionResumed(boolean sessionPresent) {
                System.out.println("Connection resumed: " + (sessionPresent ? "existing session" : "clean session"));
            }
        };

        try {
            AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(certPath, keyPath);
            if (caPath != "") {
                builder.withCertificateAuthorityFromPath(null, caPath);
            }
            builder.withConnectionEventCallbacks(callbacks)
                    .withClientId(clientId)
                    .withEndpoint(endpoint)
                    .withPort((short) port)
                    .withCleanSession(true)
                    .withProtocolOperationTimeoutMs(60000);
            if (proxyHost != "" && proxyPort > 0) {
                HttpProxyOptions proxyOptions = new HttpProxyOptions();
                proxyOptions.setHost(String.valueOf(proxyHost));
                proxyOptions.setPort(port);
                builder.withHttpProxyOptions(proxyOptions);
            }
            MqttClientConnection connection = builder.build();
            builder.close();

            CompletableFuture<Boolean> connected = connection.connect();
            try {
                boolean sessionPresent = connected.get();
                System.out.println("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
            } catch (Exception ex) {
                throw new RuntimeException("Exception occurred during connect", ex);
            }
            // Logica para suscribirse a topics al iniciar la aplicacion, los toma de bdd
            CountDownLatch countDownLatch = new CountDownLatch(1);
            List<MachinEntity> machines = machineRepository.findAll();
            List<String> topics = new ArrayList<>();
            for (MachinEntity machine : machines){
                String topicInfo = "dispensador/informacion/"+ machine.getMachineId().toString();
                String topicTransaction = "dispensador/transacciones/"+machine.getMachineId().toString();
                topics.add(topicInfo);
                topics.add(topicTransaction); //TODO : PASAR TOPICS PARA QUE SE PUEDAN ACCEDER DESDE LA BASE DE DATOS COMO EL ID, ASI NO HYA QUE MODIFICAR EL CODIGO PARA AGREGAR MAS TOPICS
            }

            List<CompletableFuture<Integer>> subscriptions = new ArrayList<>();
            for (String topic : topics) {
                CompletableFuture<Integer> subscription = connection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, (message) -> {
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    formatMessage(payload, topic);
                    System.out.println("MESSAGE TOPIC:  "+ topic + payload); // USAR PARA ADMINISTRAR LOS MENSAJES RECIBIDOS

                });
                subscriptions.add(subscription);
            }

            CompletableFuture.allOf(subscriptions.toArray(new CompletableFuture[0])).get();

            countDownLatch.await();


        } catch (CrtRuntimeException | InterruptedException | ExecutionException ex) {
            onApplicationFailure(ex);
        }

        // No se realiza la desconexión ni el cierre de la conexión aquí

        CrtResource.waitForNoResources();
        System.out.println("Complete!");
    }
    static void onApplicationFailure(Throwable cause) {
        if (isCI) {
            throw new RuntimeException("BasicPubSub execution failure", cause);
        } else if (cause != null) {
            System.out.println("Exception encountered: " + cause.toString());
        }
    }
    static String ciPropValue = System.getProperty("aws.crt.ci");
    static boolean isCI = ciPropValue != null && Boolean.valueOf(ciPropValue);

    public void formatMessage(String payload,String topic){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);

            if (topic.startsWith("dispensador/informacion/")) {
                // Mensaje de información
                String idMachine = topic.substring("dispensador/informacion/".length());
                String status = jsonNode.get("status").asText();
                String currency = jsonNode.get("currency").asText();
                String

                // Actualizar los datos de la máquina en la base de datos
                MachinEntity machine = machineRepository.findByMachineId(idMachine);
                if (machine != null) {
                    machine.setStatus(status);
                    machine.setCurrency(currency);
                    machine.setStatus(prices);
                    machine.setControl(control);
                    machineRepository.save(machine);

                    System.out.println("Datos actualizados en la base de datos para la máquina con id: " + idMachine);
                }
            } else if (topic.startsWith("dispensador/transacciones/")) {
                // Mensaje de transacciones
                String idMachine = topic.substring("dispensador/transacciones/".length());
                JsonNode transactionsNode = jsonNode.get("transactions");
                List<TransactionEntity> transactions = objectMapper.convertValue(transactionsNode, new TypeReference<List<TransactionEntity>>() {});

                // Guardar las transacciones en la base de datos
                for (TransactionEntity transaction : transactions) {
                    transaction.setIdMachine(idMachine);
                    transactionRepository.save(transaction);
                }

                System.out.println("Transacciones guardadas en la base de datos para la máquina con id: " + idMachine);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

}
