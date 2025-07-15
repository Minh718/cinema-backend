package com.movie.notificationservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.movie.notificationservice.dtos.requests.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listen(NotificationEvent event) {
        try {
            notificationService.sendNotification(event);
        } catch (Exception e) {
            // log.error("Error while processing Kafka notification", e);
        }
    }
}
