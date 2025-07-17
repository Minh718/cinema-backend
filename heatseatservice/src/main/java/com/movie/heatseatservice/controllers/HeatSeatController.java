package com.movie.heatseatservice.controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.heatseatservice.dtos.requests.HeatSeatRequest;
import com.movie.heatseatservice.dtos.responses.ApiRes;
import com.movie.heatseatservice.services.HeatSeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/heatseats")
@RequiredArgsConstructor
public class HeatSeatController {

    private final HeatSeatService heatSeatService;

    @PostMapping("/hold")
    public ApiRes<Void> holdSeats(@RequestBody HeatSeatRequest request) {
        heatSeatService.holdSeats(request);
        return ApiRes.<Void>builder().message("success").code(1000).build();
    }

    @PostMapping("/release")
    public ApiRes<Void> releaseSeats(@RequestBody HeatSeatRequest request) {
        heatSeatService.releaseSeats(request);
        return ApiRes.<Void>builder().message("success").code(1000).build();

    }
}