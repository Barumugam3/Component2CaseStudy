package com.iiht.estock.stock.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iiht.estock.stock.eventsourcing.events.AddStockEvent;
import com.iiht.estock.stock.model.Stock;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log4j2
@EnableKafka
public class KafkaAddStockEventSourcing {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${message.topic.addStock}")
    private String topicName;

    public AddStockEvent publicAddStockEvent(Stock stock) throws JsonProcessingException {
        val id = UUID.randomUUID();
        ObjectWriter objectWriter = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer();
        val json = objectWriter.writeValueAsString(stock);

        log.info("Send json {} to topic {}", json, topicName);

        kafkaTemplate.send(topicName, json);

        return AddStockEvent.builder().uuid(id).stock(stock).build();
    }
}
