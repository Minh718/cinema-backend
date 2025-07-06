package com.movie.bookingservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.bookingservice.dtos.requests.BookingRequest;
import com.movie.bookingservice.dtos.responses.ApiRes;
import com.movie.bookingservice.entities.Booking;
import com.movie.bookingservice.services.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiRes<Booking> createBooking(@RequestBody BookingRequest request) {
        return ApiRes.<Booking>builder().result(bookingService.createBooking(request)).code(1000)
                .message("Booking success")
                .build();
    }

}