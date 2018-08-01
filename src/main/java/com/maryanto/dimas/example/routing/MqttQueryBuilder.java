package com.maryanto.dimas.example.routing;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MqttQueryBuilder {

    private final Map<String, Object> queryParams = new HashMap<>();
    private final String topicSubcribe;
    private String host;
    private String username;
    private String password;
    private String mqttName;

    public MqttQueryBuilder(
            @NotNull String host,
            @NotNull String mqttName,
            @NotNull String username,
            @NotNull String password,
            @NotNull String topicToSubscibe) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.mqttName = mqttName;
        this.topicSubcribe = topicToSubscibe;

        queryParams.put("clientId", UUID.randomUUID().toString());
        queryParams.put("qualityOfService", "ExactlyOnce");
        queryParams.put("cleanSession", false);
        queryParams.put("lazySessionCreation", true);
        queryParams.put("keepAlive", 10);
        queryParams.put("host", this.host);
        queryParams.put("userName", this.username);
        queryParams.put("password", this.password);
    }

    @Override
    public String toString() {
        StringBuilder route = new StringBuilder("mqtt:");
        route.append(mqttName).append("?").append("subscribeTopicNames=").append(topicSubcribe);
        for (Map.Entry<String, Object> entry : queryParams.entrySet())
            route.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        return route.toString();
    }
}
