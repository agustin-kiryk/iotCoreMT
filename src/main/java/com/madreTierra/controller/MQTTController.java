package com.madreTierra.controller;

import com.madreTierra.dto.ComandDTO;
import com.madreTierra.utils.MQTTConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/mqtt")

public class MQTTController {
    @Autowired
    MQTTConfig mqttConfig;
    @PostMapping("/publish")
    public String publishMessaged(@RequestBody ComandDTO payload) throws IOException {

        mqttConfig.publishToShadow(payload);

        return "message Published Successfully";
    }
}
