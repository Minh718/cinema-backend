package com.movie.movieservice.repositories.httpClients;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "showtime-service")
public interface ShowTimeClient {

    @GetMapping("/showtimes/now-showing/movie-ids")
    List<Long> getNowShowingMovieIdsByCinema(@RequestParam("cinemaId") Long cinemaId);

}
