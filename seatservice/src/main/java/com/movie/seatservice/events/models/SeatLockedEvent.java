package com.movie.seatservice.events.models;

import java.time.Instant;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatLockedEvent {
    private Long bookingId;
    private String message;
    private Set<Long> seatIds; // Requested seat IDs

    private Instant timestamp;
    // getters/setters
}