package com.movie.bookingservice.repositories.httpClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.bookingservice.dtos.responses.ApiRes;
import com.movie.bookingservice.dtos.responses.ShowTimeRes;

@FeignClient(name = "showtime-service")
public interface ShowTimeClient {

    @GetMapping("internal/showtimes/{id}/bookable")
    public ApiRes<ShowTimeRes> checkShowTimeAvailable(@PathVariable Long id);
}
