package com.movie.paymentservice.events.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlPaymentCreatedEvent {

    private Long bookingId;
    private String message;
    private Instant timestamp;
    private String urlPayment;
}