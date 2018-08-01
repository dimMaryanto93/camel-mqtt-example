package com.maryanto.dimas.example.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maryanto.dimas.example.models.Suhu;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuhuConverterProcessor implements Processor {

    private final String location;

    public SuhuConverterProcessor(String location) {
        this.location = location;
    }

    private static final Logger console = LoggerFactory.getLogger(SuhuConverterProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        // log header info
        console.info("host from {} headers: {}", location, exchange.getIn().getHeaders());
        // convert byte[] to string json format
        String jsonBodyString = new String((byte[]) exchange.getIn().getBody());
        console.info("btye[] to string : {}", jsonBodyString);
        // convert json object to bean / pojo
        Suhu bodyConverted = new ObjectMapper().readValue(jsonBodyString, Suhu.class);
        exchange.getIn().setBody(bodyConverted);
    }
}
