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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiRes<Booking> createBooking(@RequestBody BookingRequest request) throws JsonProcessingException {
        return ApiRes.<Booking>builder().result(bookingService.createBooking(request)).code(1000)
                .message("Booking success")
                .build();
    }
}