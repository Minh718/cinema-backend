package com.movie.bookingservice.dtos.requests;

import com.movie.bookingservice.enums.PaymentMethod;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentReq {
    private String orderId;
    private String amount;
    private String orderInfo;
    private PaymentMethod paymentMethod;
}