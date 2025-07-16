package com.movie.paymentservice.events.models;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.movie.paymentservice.enums.PaymentMethod;

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