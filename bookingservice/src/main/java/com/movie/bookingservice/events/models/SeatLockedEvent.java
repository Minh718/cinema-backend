package com.movie.bookingservice.events.models;

import java.time.Instant;
import java.util.Set;

import com.movie.bookingservice.dtos.requests.SeatReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatLockedEvent {
    private Long bookingId;
    private String message;
    private Instant timestamp;

    // getters/setters
}