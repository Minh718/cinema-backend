package com.movie.notificationservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.notificationservice.dtos.requests.NotificationReq;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listen(String message) {
        try {
            NotificationReq request = objectMapper.readValue(message, NotificationReq.class);
            notificationService.sendNotification(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
