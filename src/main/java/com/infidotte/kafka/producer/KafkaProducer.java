package com.infidotte.kafka.producer;

import com.infidotte.kafka.service.KafkaMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaMessagingService service;

    public void sendAsyncMessage(String message) {
        service.sendAsync(message);
    }

    public CompletableFuture<SendResult<String, String>> sendSyncMessage(String message){
        return service.sendSync(message);
    }
}
