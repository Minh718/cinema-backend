package com.movie.paymentservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.paymentservice.dtos.requests.CreatePaymentReq;
import com.movie.paymentservice.dtos.requests.MomoCallback;
import com.movie.paymentservice.dtos.responses.ApiRes;
import com.movie.paymentservice.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ApiRes<String> createPayment(@RequestBody CreatePaymentReq request) {
        String response = paymentService.createPayment(request);
        return ApiRes.<String>builder().code(200).message("Success").result(response).build();
    }

    @PostMapping("/ipn")
    public ApiRes<String> handleMomoNotify(@RequestBody MomoCallback payload) {
        try {
            paymentService.processMomoCallback(payload);
            return ApiRes.<String>builder().code(200).message("Success").result("OK").build();
        } catch (Exception e) {
            return ApiRes.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
        }
    }

}
