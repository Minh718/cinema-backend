package com.movie.paymentservice.controllers;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.paymentservice.dtos.requests.MomoCallback;
import com.movie.paymentservice.dtos.responses.ApiRes;
import com.movie.paymentservice.services.PaymentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    @NonFinal
    @Value("${frontend_host:http://localhost:3000}")
    private String frontend_host;

    // @PostMapping("/create")
    // public ApiRes<String> createPayment(@RequestBody CreatePaymentReq request,
    // HttpServletRequest httpServletRequest) {
    // String response = paymentService.createPayment(request, httpServletRequest);
    // return
    // ApiRes.<String>builder().code(200).message("Success").result(response).build();
    // }

    @PostMapping("/ipn")
    public ApiRes<String> handleMomoNotify(@RequestBody MomoCallback payload) {
        try {
            paymentService.processMomoCallback(payload);
            return ApiRes.<String>builder().code(200).message("Success").result("OK").build();
        } catch (Exception e) {
            return ApiRes.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
        }
    }

    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(@RequestParam Map<String, String> reqParams,
            HttpServletResponse response) throws IOException {
        paymentService.processVnpayCallback(reqParams);
        String status = reqParams.get("vnp_ResponseCode");
        if (status.equals("00")) {
            response.sendRedirect(frontend_host + "/payment-success");
        } else {
            response.sendRedirect(frontend_host + "/payment-fail");
        }
    }
}
