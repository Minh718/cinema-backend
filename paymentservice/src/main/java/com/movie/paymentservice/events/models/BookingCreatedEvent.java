package com.movie.paymentservice.events.models;

import java.util.Set;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingCreatedEvent {

    private String orderId;
    private Long bookingId;
    private String amount;
    private String orderInfo;
    private PaymentMethod paymentMethod;
    private String clientIp;
}