package com.movie.bookingservice.events.models;

import java.time.Instant;

import com.movie.bookingservice.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompletedEvent {
    private Long bookingId;
    private String message;
    private Instant timestamp;
}