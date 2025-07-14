package com.movie.bookingservice.dtos.requests;

import java.util.Set;

import com.movie.bookingservice.enums.PaymentMethod;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private Long showTimeId;
    private Long roomId;
    private Double totalPrice;
    private PaymentMethod paymentMethod;
    private Set<Long> seatIds;
}