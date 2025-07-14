package com.movie.paymentservice.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.paymentservice.dtos.requests.NotificationReq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "notifications";

    public void sendNotification(NotificationReq request) {
        try {
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(TOPIC, message);
            log.info(" Sent notification to Kafka: {}", message);
        } catch (JsonProcessingException e) {
            log.error(" Failed to serialize NotificationRequest", e);
        }
    }
}