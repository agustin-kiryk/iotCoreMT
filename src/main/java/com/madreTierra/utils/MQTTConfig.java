package com.madreTierra.utils;

import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.madreTierra.dto.ComandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
@Configuration
public class MQTTConfig {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private AwsConfig iotClient;
    public void publishToShadow(ComandDTO comand) throws IOException {

        //ComandDTO comand = new ComandDTO();
        //mqttSubscriber("dispensador/informacion/230517_1");
        String topic2 = comand.getComand();
        String topic = "dispensador/ret_informacion/230517_1";
        Map<String, Object> payloadList = comand.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(payloadList);
        //String payload = "{\"state\":{\"reported\":{\"sensor\":3.0}}}";
        //String payload =comand.getPayload();

        ObjectWriter objectWriter = objectMapper.writer();
        String processedJson = objectWriter.writeValueAsString(comand.getPayload());
        ByteBuffer bb = StandardCharsets.UTF_8.encode(processedJson);

        System.out.println(processedJson);
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.withPayload(bb);
        publishRequest.withTopic(topic2);
        publishRequest.setQos(0);
        GetThingShadowRequest shadowRequest = new GetThingShadowRequest();
        iotClient.getIotDataClient(appConfig).publish(publishRequest);

        System.out.println("message Published successfully");


    }
}
