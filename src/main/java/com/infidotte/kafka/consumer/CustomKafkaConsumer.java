package com.infidotte.kafka.consumer;


import com.infidotte.kafka.entity.Response;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CustomKafkaConsumer {

    private final KafkaConsumer<String, String> consumer;

    @Value("${topic.kafka-topic}")
    private String topic;
    @Value("${topic.group-id}")
    private String groupId;

    @KafkaListener(topics = "${topic.kafka-topic}", groupId = "${topic.group-id}")
    public Response read(String message) {
        Response response = new Response(message);
        System.out.printf("Received message %s and converted to record\n", message);
        return response;
    }

    @KafkaListener(topics = "${topic.kafka-topic}", groupId = "${topic.group-id}")
    public String listen(String message) {
        System.out.println("Received message: " + message);
        return message;
    }

    public String listen() {
        ConsumerRecords<String, String> records = consumerRecords();
        return records.iterator().next().value();
    }

    private ConsumerRecords<String, String> consumerRecords() {
        return consumer.poll(Duration.ofSeconds(1));
    }
}
