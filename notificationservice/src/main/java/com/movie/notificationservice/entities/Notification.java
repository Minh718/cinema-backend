package com.movie.notificationservice.entities;

import java.time.LocalDateTime;

import com.movie.notificationservice.enums.TypeNotification;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private TypeNotification type; // EMAIL, PUSH, SMS
    private Boolean sent;

    private LocalDateTime createdAt;
}
