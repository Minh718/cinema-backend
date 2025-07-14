package com.movie.paymentservice.repositories.httpCliens;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.paymentservice.enums.PaymentStatus;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @PostMapping("/bookings/internal/update-payment-status-and-redis-seat-ids")
    void updateStatusToPaidAndRedisSeatIds(@RequestParam("bookingId") Long bookingId,
            @RequestParam PaymentStatus status);
}