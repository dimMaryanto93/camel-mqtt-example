package com.maryanto.dimas.example.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maryanto.dimas.example.models.Suhu;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MqttPayloadStoreComponent extends RouteBuilder {

    private static final Logger console = LoggerFactory.getLogger(MqttPayloadStoreComponent.class);

    @Override
    public void configure() throws Exception {
        from("mqtt:job-message-store-mqtt?subscribeTopicNames=ruang/suhu")
                .routeId("ruang-suhu")
                .group("job-message-store")
                .tracing()
//                .log("the body is ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String jsonBodyString = new String((byte[]) exchange.getIn().getBody());
                        Suhu bodyConverted = new ObjectMapper().readValue(jsonBodyString, Suhu.class);
                        console.info("body application/json conversion: {}", bodyConverted);
                        exchange.getIn().setBody(bodyConverted);
                    }
                })
                .to("bean:mqttStoreSuhuRepository?method=createStoreSuhu(${body})");
//                .autoStartup(true);
    }
}
