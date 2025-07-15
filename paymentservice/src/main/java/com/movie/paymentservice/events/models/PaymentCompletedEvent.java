package com.movie.paymentservice.events.models;

import com.movie.paymentservice.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompletedEvent {
    private Long bookingId;
    private PaymentStatus status;
}