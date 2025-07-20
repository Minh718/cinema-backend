package com.movie.bookingservice.dtos.requests;

import java.math.BigDecimal;
import java.util.Set;

import com.movie.bookingservice.enums.PaymentMethod;

import lombok.Data;

@Data
public class BookingRequest {
    private PaymentMethod paymentMethod;
    private Double totalPrice;
    private Long showTimeId;
    private Set<SeatReq> seats;
}