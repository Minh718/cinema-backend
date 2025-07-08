package com.movie.notificationservice.dtos.requests;

import com.movie.notificationservice.enums.TypeNotification;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private TypeNotification type;
}
