package com.iiht.estock.stock.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiht.estock.stock.model.Stock;
import com.iiht.estock.stock.service.StockQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Log4j2
@Component
@RequiredArgsConstructor
public class KafkaDeleteStockEventListener {

    private final StockQueryService stockQueryService;
    private final CountDownLatch latch = new CountDownLatch(3);

    @KafkaListener(topics = "${message.topic.deleteStock}")
    public void listen(ConsumerRecord<String, String> stringConsumerRecord) throws JsonProcessingException {

        log.info("Consumer read record {}", stringConsumerRecord.value());
        Stock stock = new ObjectMapper().readerFor(Stock.class).readValue(stringConsumerRecord.value());
        stockQueryService.deleteStock(stock);
        log.info("Deleted stock {} in reader database", stock);
        latch.countDown();
    }
}
