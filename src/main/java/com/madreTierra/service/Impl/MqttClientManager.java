package com.madreTierra.service.Impl;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
@Component
public class MqttClientManager {
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

            CountDownLatch countDownLatch = new CountDownLatch(1);
            //GESTIONAR SUSCRIOCIONES A LISTA DE TOPICS SEGUN EL CLIENTE
            List<String> topics = Arrays.asList("dispensador/informacion/230517_1", "dispensador/informacion/230517_2", "dispensador/informacion/230517_3"); // Lista de topics a los que te quieres suscribir
             //TODO: MODIFICAR LA LISTA PARA QUE SE SUCRIBAN TODOS LOS CLIENTES CREADOS
            List<CompletableFuture<Integer>> subscriptions = new ArrayList<>();
            for (String topic : topics) {
                CompletableFuture<Integer> subscription = connection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, (message) -> {
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    System.out.println("MESSAGE TOPIC:  "+ topic + payload);
                    // No se realiza la desconexión ni el cierre de la conexión aquí
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

}
