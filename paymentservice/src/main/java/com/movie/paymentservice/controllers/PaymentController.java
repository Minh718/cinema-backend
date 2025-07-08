package com.movie.paymentservice.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.paymentservice.dtos.responses.ApiRes;
import com.movie.paymentservice.dtos.responses.MomoPaymentResponse;
import com.movie.paymentservice.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/momo")
    public ApiRes<MomoPaymentResponse> createPayment(@RequestParam String orderId,
            @RequestParam String amount,
            @RequestParam String orderInfo) {
        MomoPaymentResponse response = paymentService.createMomoPayment(orderId, amount, orderInfo);
        return ApiRes.<MomoPaymentResponse>builder().code(200).message("Success").result(response).build();
    }
}
