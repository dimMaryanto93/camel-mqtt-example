package com.maryanto.dimas.example.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maryanto.dimas.example.models.Suhu;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MqttPayloadStoreComponent extends RouteBuilder {

    private static final Logger console = LoggerFactory.getLogger(MqttPayloadStoreComponent.class);

    @Override
    public void configure() throws Exception {
        // subcribe topic : ruang/suhu -> qos : 2
        from("mqtt:job-message-store-mqtt?subscribeTopicNames=ruang/suhu&willQos=ExactlyOnce")
                .routeId("ruang-suhu")
                .group("job-message-store")
                .tracing()
                .log(LoggingLevel.INFO, "subcribe ${body}")
                .process(exchange -> {
                    // convert byte[] to string json format
                    String jsonBodyString = new String((byte[]) exchange.getIn().getBody());
                    console.info("btye[] to string : {}", jsonBodyString);
                    // convert json object to bean / pojo
                    Suhu bodyConverted = new ObjectMapper().readValue(jsonBodyString, Suhu.class);
                    exchange.getIn().setBody(bodyConverted);
                })
                .to("bean:mqttStoreSuhuRepository?method=createStoreSuhu(${body})");
    }
}
