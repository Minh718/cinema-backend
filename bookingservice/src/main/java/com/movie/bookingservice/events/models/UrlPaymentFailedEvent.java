package com.movie.bookingservice.events.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlPaymentFailedEvent {

    private Long bookingId;
    private String message;
    private Instant timestamp;

}