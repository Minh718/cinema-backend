package com.movie.showtimeservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.dtos.responses.ShowTimeDetailRes;
import com.movie.showtimeservice.services.ShowTimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/showtimes")
@RequiredArgsConstructor
public class InternalShowTimeController {
    private final ShowTimeService showTimeService;

    @GetMapping("/{id}/bookable")
    public ApiRes<ShowTimeDetailRes> checkShowTimeAvailable(@PathVariable Long id) {
        ShowTimeDetailRes showTime = showTimeService.getBookableDetailShowTime(id);
        return ApiRes.<ShowTimeDetailRes>builder()
                .result(showTime)
                .code(1000)
                .message("successfully")
                .build();
    }
}
