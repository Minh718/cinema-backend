package com.movie.paymentservice.events.publishers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.paymentservice.events.models.NotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaPublisher {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private static final String TOPIC = "notifications";

    public void pubishNotificationEvent(NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event);
        log.info(" Sent notification to Kafka: {}", event);

    }
}