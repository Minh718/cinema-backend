package com.movie.showtimeservice.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.showtimeservice.dtos.requests.AutoAssignRequest;
import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.entities.ShowTime;
import com.movie.showtimeservice.services.ShowTimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowTimeController {

    private final ShowTimeService showTimeService;

    @PostMapping("/auto")
    public ApiRes<ShowTime> autoAssignShowTime(@RequestBody AutoAssignRequest request) {
        return ApiRes.<ShowTime>builder()
                .code(1000)
                .result(showTimeService.autoAssign(request))
                .message("Add product successfully")
                .build();
    }
}
