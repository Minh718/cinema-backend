package com.movie.paymentservice.dtos.requests;

import com.movie.paymentservice.enums.TypeNotification;

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
