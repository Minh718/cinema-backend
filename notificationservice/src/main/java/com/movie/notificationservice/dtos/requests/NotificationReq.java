package com.movie.notificationservice.dtos.requests;

import com.movie.notificationservice.enums.MailType;
import com.movie.notificationservice.enums.TypeNotification;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class NotificationReq {
    private Long userId;
    private String title;
    private String to;
    private String message;
    private TypeNotification type;
    private EmailTemplateInfo emailTemplate;
}
