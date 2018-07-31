package com.maryanto.dimas.example.repository;

import com.maryanto.dimas.example.models.Suhu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("mqttStoreSuhuRepository")
public class MqttStoreSuhuRepository {

    private final static Logger console = LoggerFactory.getLogger(MqttStoreSuhuRepository.class);

    public String createStoreSuhu(Suhu suhu) {
        console.info("{}", suhu);
        return "berhasil disimpan!";
    }
}
