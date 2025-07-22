package com.movie.showtimeservice.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.showtimeservice.dtos.requests.ShowTimeGroupCreateReq;
import com.movie.showtimeservice.dtos.responses.ApiRes;
import com.movie.showtimeservice.dtos.responses.ShowTimeDetailRes;
import com.movie.showtimeservice.dtos.responses.ShowTimeGroupRes;
import com.movie.showtimeservice.dtos.responses.ShowTimeRes;
import com.movie.showtimeservice.dtos.responses.ShowTimesOfMovieAndDateRes;
import com.movie.showtimeservice.entities.ShowTimeGroup;
import com.movie.showtimeservice.services.ShowTimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
public class ShowTimeController {

        private final ShowTimeService showTimeService;

        @PostMapping("/manager/create")
        public ApiRes<ShowTimeGroup> createGroupShowTime(@RequestBody ShowTimeGroupCreateReq request) {
                return ApiRes.<ShowTimeGroup>builder()
                                .code(1000)
                                .result(showTimeService.createGroupShowTime(request))
                                .message("Add product successfully")
                                .build();
        }

        @GetMapping("/manager/{showTimeGroupId}/add-show-time")
        public ApiRes<ShowTimeRes> addShowTimeToGroupShowTime(@PathVariable Long showTimeGroupId) {
                return ApiRes.<ShowTimeRes>builder()
                                .code(1000)
                                .result(showTimeService.addShowTimeToGroupShowTime(showTimeGroupId))
                                .message("Add product successfully")
                                .build();
        }

        @GetMapping("/public/dates")
        public ApiRes<List<LocalDate>> getFutureShowDatesByMovieId(@RequestParam("movieId") Long movieId,
                        @RequestParam("cinemaId") Long cinemaId) {
                List<LocalDate> showDates = showTimeService.getFutureShowDatesByMovieIdAndCinemaId(movieId, cinemaId);
                return ApiRes.<List<LocalDate>>builder()
                                .result(showDates)
                                .code(1000)
                                .message("successfully")
                                .build();
        }

        @GetMapping("/public/times")
        public ApiRes<ShowTimesOfMovieAndDateRes> getShowTimesByMovieIdAndDate(
                        @RequestParam("movieId") Long movieId,
                        @RequestParam("cinemaId") Long cinemaId,
                        @RequestParam("date") LocalDate date) {

                ShowTimesOfMovieAndDateRes showTimes = showTimeService.getShowTimesByMovieIdAndCinemaIdAndDate(movieId,
                                cinemaId, date);
                return ApiRes.<ShowTimesOfMovieAndDateRes>builder()
                                .result(showTimes)
                                .code(1000)
                                .message("successfully")
                                .build();
        }

        @GetMapping("/now-showing/movie-ids")
        List<Long> getNowShowingMovieIdsByCinema(@RequestParam("cinemaId") Long cinemaId) {
                return showTimeService.getNowShowingMovieIdsByCinemaId(cinemaId);
        }

        @GetMapping("/{id}/bookable")
        public ApiRes<ShowTimeDetailRes> getBookableDetailShowTime(@PathVariable Long id) {
                ShowTimeDetailRes showTime = showTimeService.getBookableDetailShowTime(id);
                return ApiRes.<ShowTimeDetailRes>builder()
                                .result(showTime)
                                .code(1000)
                                .message("successfully")
                                .build();
        }

        @GetMapping("/showtimegroups")
        public ApiRes<List<ShowTimeGroupRes>> getShowTimeGroupsInDate(
                        @RequestParam("date") LocalDate date) {
                return ApiRes.<List<ShowTimeGroupRes>>builder()
                                .result(showTimeService.getShowTimeGroupsInDate(date))
                                .code(1000)
                                .message("successfully")
                                .build();
        }

}
