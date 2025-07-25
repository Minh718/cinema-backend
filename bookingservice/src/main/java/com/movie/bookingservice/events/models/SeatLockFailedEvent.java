package com.movie.bookingservice.events.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatLockFailedEvent {
    private Long bookingId;
    private String message;
    private Instant timestamp;
    // getters/setters
}