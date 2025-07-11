package com.movie.showtimeservice.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie.showtimeservice.dtos.requests.AutoAssignRequest;
import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.entities.ShowTime;
import com.movie.showtimeservice.services.ShowTimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/showtimes")
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

    @GetMapping("/public/{movieId}/dates")
    public ApiRes<List<LocalDate>> getFutureShowDatesByMovieId(@PathVariable("movieId") Long movieId) {
        List<LocalDate> showDates = showTimeService.getFutureShowDatesByMovieId(movieId);
        return ApiRes.<List<LocalDate>>builder()
                .result(showDates)
                .code(1000)
                .message("successfully")
                .build();
    }

    @GetMapping("/public/{movieId}/times")
    public ApiRes<List<ShowTime>> getShowTimesByMovieIdAndDate(
            @PathVariable("movieId") Long movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<ShowTime> showTimes = showTimeService.getShowTimesByMovieIdAndDate(movieId, date);
        return ApiRes.<List<ShowTime>>builder()
                .result(showTimes)
                .code(1000)
                .message("successfully")
                .build();
    }

}
