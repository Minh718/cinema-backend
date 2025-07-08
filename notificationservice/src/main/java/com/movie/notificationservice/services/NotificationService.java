package com.movie.notificationservice.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.movie.notificationservice.dtos.requests.NotificationRequest;
import com.movie.notificationservice.entities.Notification;
import com.movie.notificationservice.enums.TypeNotification;
import com.movie.notificationservice.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FirebaseService firebaseService; // optional stub

    public void sendNotification(NotificationRequest request) {
        // Save log
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .sent(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        if (request.getType() == TypeNotification.PUSH) {
            boolean success = firebaseService.sendPush(request.getUserId(), request.getTitle(), request.getMessage());
            notification.setSent(success);
        }

        notificationRepository.save(notification);
    }
}
