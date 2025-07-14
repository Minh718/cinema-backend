package com.movie.bookingservice.repositories.httpClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.movie.bookingservice.dtos.requests.CreatePaymentReq;
import com.movie.bookingservice.dtos.responses.ApiRes;

@FeignClient(name = "payment-service")
public interface PaymentClient {

        @PostMapping("/payments/create")
        ApiRes<String> createPayment(@RequestBody CreatePaymentReq request);

}
