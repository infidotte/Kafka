package com.infidotte.kafka.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaMessagingService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.kafka-topic}")
    private String topic;

    public void sendAsync(String message) {
        kafkaTemplate.send(topic, message);
    }

    public CompletableFuture<SendResult<String, String>> sendSync(String message){
        return kafkaTemplate.send(topic, message);
    }
}