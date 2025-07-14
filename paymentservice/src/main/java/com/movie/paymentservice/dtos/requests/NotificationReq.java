package com.movie.paymentservice.dtos.requests;

import com.movie.paymentservice.enums.TypeNotification;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReq {
    private Long userId;
    private String title;
    private String to;
    private String message;
    private TypeNotification type;
    private EmailTemplateInfo emailTemplate;
}
