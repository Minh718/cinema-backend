package com.movie.seatservice.events.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailedEvent {
    private Long bookingId;
    private String message;
    private Instant timestamp;
}