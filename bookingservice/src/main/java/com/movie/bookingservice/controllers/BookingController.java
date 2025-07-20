package com.movie.bookingservice.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.bookingservice.dtos.requests.BookingRequest;
import com.movie.bookingservice.dtos.responses.ApiRes;
import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.enums.PaymentStatus;
import com.movie.bookingservice.services.BookingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiRes<Booking> createBooking(@RequestBody BookingRequest request, HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        String tokenBearer = httpServletRequest.getHeader("Authorization");
        String userId = httpServletRequest.getHeader("X-User-Id");
        String clientIp = getClientIpAddress(httpServletRequest);
        return ApiRes.<Booking>builder().result(bookingService.createBooking(request, clientIp, tokenBearer, userId))
                .code(1000)
                .message("Booking success")
                .build();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}