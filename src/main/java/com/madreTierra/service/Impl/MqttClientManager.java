package com.madreTierra.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madreTierra.entity.MachinEntity;
import com.madreTierra.entity.TransactionEntity;
import com.madreTierra.enumeration.LightSwitch;
import com.madreTierra.enumeration.ValveFill;
import com.madreTierra.enumeration.ValveWash;
import com.madreTierra.enumeration.WaterPumpSwich;
import com.madreTierra.repository.MachineRepository;
import com.madreTierra.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.CrtResource;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.http.HttpProxyOptions;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static java.awt.SystemColor.control;

@Component
public class MqttClientManager {

    @Autowired
    MachineRepository machineRepository;
    @Autowired
    TransactionRepository transactionRepository;
    static String certPath = "certs/8210489b5e-certificate_pem.crt";
    static String keyPath = "certs/8210489b5e-private_pem.key";
    static String caPath = "certs/AmazonRootCA1.pem";
    static String clientId = "54564646";
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
            for (MachinEntity machine : machines) {
                String topicInfo = "dispensador/informacion/" + machine.getMachineId().toString();
                String topicTransaction = "dispensador/transacciones/" + machine.getMachineId().toString();
                String topicBalance = "dispensador/balance/" + machine.getMachineId().toString();
                topics.add(topicInfo);
                topics.add(topicTransaction); //TODO : PASAR TOPICS PARA QUE SE PUEDAN ACCEDER DESDE LA BASE DE DATOS COMO EL ID, ASI NO HYA QUE MODIFICAR EL CODIGO PARA AGREGAR MAS TOPICS
            }

            List<CompletableFuture<Integer>> subscriptions = new ArrayList<>();
            for (String topic : topics) {
                CompletableFuture<Integer> subscription = connection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, (message) -> {
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    // Verificar si el formato del mensaje es válido
                    boolean isValidFormat = formatMessage(payload, topic);

                    if (isValidFormat) {
                        System.out.println("MESSAGE TOPIC:  " + topic + payload); // Utiliza para administrar los mensajes recibidos
                    }

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

    public Boolean formatMessage(String payload, String topic) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode == null) {
                System.out.println("Mensaje con formato incorrecto en el topic: " + topic);
                return false;
            }

            if (topic.startsWith("dispensador/informacion/")) {  //---------INFORMACION------------//
                // Procesar mensaje de información
                if (!jsonNode.has("status") || !jsonNode.has("currency") || !jsonNode.has("price")
                        || !jsonNode.has("water_pump") || !jsonNode.has("light") || !jsonNode.has("valve_fill")
                        || !jsonNode.has("valve_wash")) {
                    System.out.println("Mensaje con formato incorrecto en el topic: " + topic + "  REVISAR EL FORMATO DE: " + payload);
                    return false;
                }

                // Obtener los datos de la máquina
                String machineId = topic.substring("dispensador/informacion/".length());
                String status = jsonNode.get("status").asText();
                String currency = jsonNode.get("currency").asText();
                String price = jsonNode.get("price").asText();
                String waterPump = jsonNode.get("water_pump").asText();
                String light = jsonNode.get("light").asText();
                String valveFill = jsonNode.get("valve_fill").asText();
                String valveWash = jsonNode.get("valve_wash").asText();

                // Actualizar los datos de la máquina en la base de datos
                MachinEntity machine = machineRepository.findByMachineId(machineId);
                if (machine != null) {
                    machine.setStatus(status);
                    machine.setCurrency(currency);
                    machine.setPrice(Double.valueOf(price));
                    machine.setWaterPump(WaterPumpSwich.valueOf(waterPump));
                    machine.setLight(LightSwitch.valueOf(light));
                    machine.setValveFill(ValveFill.valueOf(valveFill));
                    machine.setValveWash(ValveWash.valueOf(valveWash));
                    machineRepository.save(machine);

                    System.out.println("Datos actualizados en la base de datos para la máquina con id: " + machineId);
                }
            } else if (topic.startsWith("dispensador/transacciones/")) {  //---------TRANSACCIONES------------//
                // Procesar mensaje de transacciones
                if (!jsonNode.has("idMachine") || !jsonNode.has("currency") || !jsonNode.has("transactions")) {
                    System.out.println("Mensaje con formato incorrecto en el topic: " + topic + "  REVISAR EL FORMATO DE: " + payload);
                    return false;
                }

                String machineId = jsonNode.get("idMachine").asText();
                String currency = jsonNode.get("currency").asText();
                JsonNode transactionsNode = jsonNode.get("transactions");

                // Procesar cada transacción
                for (JsonNode transactionNode : transactionsNode) {
                    if (!transactionNode.has("idTransaction") || !transactionNode.has("amount")
                            || !transactionNode.has("dispensedWater") || !transactionNode.has("date")) {
                        System.out.println("Transacción con formato incorrecto en el topic: " + topic + "  REVISAR EL FORMATO DE: " + payload);
                        continue;
                    }

                    String idTransaction = transactionNode.get("idTransaction").asText();
                    Double amount = transactionNode.get("amount").asDouble();
                    Double dispensedWater = transactionNode.get("dispensedWater").asDouble();
                    String dateStr = transactionNode.get("date").asText();

                    // Convertir la cadena de fecha a un objeto LocalDateTime
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
                    LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
                    System.out.println("mensaje procesado ok ");

                    // Verificar si la transacción ya existe en la base de datos para el mismo idTransaction y machineId
                    List<TransactionEntity> existingTransactions = transactionRepository.findByTransactionIdAndMachineId(idTransaction, machineId);
                    if (!existingTransactions.isEmpty()) {
                        // Ya existe una transacción con el mismo idTransaction y misma máquina
                        System.out.println("Transacción ya existe para la máquina con id: " + machineId);
                        continue;
                    }

                    // Si no encontramos una transacción con el mismo idTransaction y misma máquina, entonces la creamos y guardamos
                    MachinEntity machine = machineRepository.findByMachineId(machineId);
                    if (machine == null) {
                        // Máquina no encontrada, puedes manejar esto según tus necesidades
                        continue;
                    }

                    TransactionEntity transaction = new TransactionEntity();
                    transaction.setMachine(machine);
                    transaction.setIdTransaction(String.valueOf(Long.valueOf(idTransaction)));
                    transaction.setAmount(amount);
                    transaction.setMachineId(machineId);
                    transaction.setCurrency(currency);
                    transaction.setDate(date);
                    transaction.setDispensedWater(dispensedWater);
                    transactionRepository.save(transaction);
                    System.out.println("Transacción guardada en la base de datos para la máquina con id: " + machineId);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("ERROR al formatear el mensaje, revisar formato de:  " + payload);
            return false;
        }
    }

}
/*if(topic.startsWith("dispensador/balance/")){//---------BALANCE------------//
                if (!jsonNode.has("dayTransactions")) {
                    System.out.println("Mensaje con formato incorrecto en el topic: " + topic + "  REVISAR EL FORMATO DE: " + payload);
                    return false;
                }

                JsonNode transactionsNode = jsonNode.get("dayTransactions");

                for (JsonNode transactionNode : transactionsNode) {
                    if (!transactionNode.has("litersWater") || !transactionNode.has("totalTransactions")
                            || !transactionNode.has("totalAmount") || !transactionNode.has("date")) {
                        System.out.println("Transacción con formato incorrecto en el topic: " + topic + "  REVISAR EL FORMATO DE: " + payload);
                        continue;
                    }
                }
            }*/