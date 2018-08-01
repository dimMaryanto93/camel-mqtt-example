package com.maryanto.dimas.example.routing;

import com.maryanto.dimas.example.processor.SuhuConverterProcessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("camel.component.mqtt.bogor")
public class RouteSuhuBogor extends RouteBuilder {
    private static final Logger console = LoggerFactory.getLogger(RouteSuhuBogor.class);

    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String password;


    @Override
    public void configure() throws Exception {
        // subcribe topic : ruang/suhu -> qos : 2
        StringBuilder route = new StringBuilder("mqtt:location-bogor?subscribeTopicNames=ruang/suhu")
                .append("&qualityOfService=ExactlyOnce")
                .append("&host=").append(host)
                .append("&userName=").append(userName)
                .append("&password=").append(password);

        from(route.toString())
                .routeId("ruang-suhu-bogor")
                .group("job-message-store")
                .tracing()
                .log(LoggingLevel.INFO, "subcribed ${body}")
                .process(new SuhuConverterProcessor())
                .log(LoggingLevel.INFO, "conversion to json: ${body}")
                .to("bean:mqttStoreSuhuRepository?method=createStoreSuhu(${body})")
                .log("${body}");
    }
}
