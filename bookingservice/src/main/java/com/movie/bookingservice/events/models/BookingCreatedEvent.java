package com.movie.bookingservice.events.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.movie.bookingservice.dtos.requests.SeatReq;
import com.movie.bookingservice.enums.PaymentMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingCreatedEvent {

    private Long bookingId;
    // private Long userId;

    private Long roomId;
    private Long showTimeId;

    private Set<SeatReq> seats; // Requested seat IDs

    private String orderId;
    private String amount;
    private String orderInfo;
    private PaymentMethod paymentMethod;
    private String clientIp;
}