package com.movie.paymentservice.dtos.requests;

import com.movie.paymentservice.enums.PaymentMethod;

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