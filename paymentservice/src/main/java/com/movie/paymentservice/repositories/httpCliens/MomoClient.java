package com.movie.paymentservice.repositories.httpCliens;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.movie.paymentservice.dtos.requests.MomoPaymentReq;
import com.movie.paymentservice.dtos.responses.MomoPaymentRes;

import feign.Headers;

@FeignClient(name = "momoClient", url = "${momo.endpoint}")
public interface MomoClient {

    @PostMapping
    @Headers("Content-Type: application/json") // optional
    MomoPaymentRes createMomoPayment(@RequestBody MomoPaymentReq request);
}