package com.movie.notificationservice.dtos.requests;

import com.movie.notificationservice.enums.MailType;
import com.movie.notificationservice.enums.TypeNotification;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long userId;
    private String title;
    private String to;
    private String message;
    private TypeNotification type;
    private EmailTemplateInfo emailTemplate;
}
