package com.madreTierra.utils;

import com.madreTierra.service.Impl.MqttClientManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {
    private final MqttClientManager mqttClientManager;

    public AppStartupRunner( MqttClientManager mqttClientManager) {
        this.mqttClientManager = mqttClientManager;
    }

    @Override
    public void run(String... args) {
        mqttClientManager.call();
        // Otras lógicas de inicialización de la aplicación
    }
}
