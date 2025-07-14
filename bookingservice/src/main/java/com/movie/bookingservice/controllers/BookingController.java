package com.movie.bookingservice.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiRes<Booking> createBooking(@RequestBody BookingRequest request) {
        return ApiRes.<Booking>builder().result(bookingService.createBooking(request)).code(1000)
                .message("Booking success")
                .build();
    }

    @GetMapping("/{showtimeId}/booked-seats")
    public ApiRes<Set<Long>> getBookedSeats(@PathVariable("showtimeId") Long showtimeId) {
        return ApiRes.<Set<Long>>builder().result(bookingService.getBookedSeatIds(showtimeId)).code(1000)
                .message("get booked seats success")
                .build();
    }

    @GetMapping("/{showtimeId}/heat-seats")
    public ApiRes<Set<Long>> getHeatSeatIds(@PathVariable("showtimeId") Long showtimeId) {
        return ApiRes.<Set<Long>>builder().result(bookingService.getHeatSeatIds(showtimeId)).code(1000)
                .message("get heat seats success")
                .build();
    }

    @PostMapping("/bookings/internal/update-payment-status-and-redis-seat-ids")
    public void updateStatusToPaidAndRedisSeatIds(@RequestParam("bookingId") Long bookingId,
            @RequestParam PaymentStatus status) {
        bookingService.updateStatusToPaidAndRedisSeatIds(bookingId, status);
    }
}