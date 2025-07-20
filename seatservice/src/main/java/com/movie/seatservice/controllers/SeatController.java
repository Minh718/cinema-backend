package com.movie.seatservice.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.seatservice.dtos.responses.ApiRes;
import com.movie.seatservice.services.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{showtimeId}/booked")
    public ApiRes<Set<Long>> getBookedSeats(@PathVariable("showtimeId") Long showtimeId) {
        return ApiRes.<Set<Long>>builder().result(seatService.getBookedSeatIds(showtimeId)).code(1000)
                .message("get booked seats success")
                .build();
    }

    @GetMapping("/{showtimeId}/heat")
    public ApiRes<Set<Long>> getHeatSeatIds(@PathVariable("showtimeId") Long showtimeId) {
        return ApiRes.<Set<Long>>builder().result(seatService.getHeatSeatIds(showtimeId)).code(1000)
                .message("get heat seats success")
                .build();
    }
}