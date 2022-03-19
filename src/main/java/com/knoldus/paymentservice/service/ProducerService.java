package com.knoldus.paymentservice.service;

import com.knoldus.paymentservice.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    private static final String TOPIC = "order-status";

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public String sendDataToKafka(String result) {
        logger.info(String.format("#### -> Producing message -> %s", result));
        kafkaTemplate.send(TOPIC, result);
        return TOPIC;
    }

    public void sendStatus(String status) {
        this.sendDataToKafka(status);
    }
}
