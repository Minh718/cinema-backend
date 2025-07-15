package com.movie.notificationservice.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.movie.notificationservice.dtos.requests.NotificationEvent;
import com.movie.notificationservice.entities.Notification;
import com.movie.notificationservice.enums.TypeNotification;
import com.movie.notificationservice.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FirebaseService firebaseService; // optional stub
    private final EmailService htmlMailService; // optional stub

    public void sendNotification(NotificationEvent request) {
        boolean sent = false;

        switch (request.getType()) {
            case TypeNotification.PUSH ->
                sent = firebaseService.sendPush(request.getUserId(), request.getTitle(), request.getMessage());
            case TypeNotification.EMAIL -> {
                sent = htmlMailService.sendEmail(
                        request.getTo(),
                        request.getMessage(),
                        request.getEmailTemplate());
            }
            case TypeNotification.SMS -> {
            }
        }

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .sent(sent)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}
