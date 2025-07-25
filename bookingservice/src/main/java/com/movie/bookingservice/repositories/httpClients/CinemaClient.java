package com.movie.bookingservice.repositories.httpClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.bookingservice.dtos.responses.ApiRes;
import com.movie.bookingservice.dtos.responses.CinemaRes;

import jakarta.persistence.Cacheable;

@FeignClient(name = "cinema-service")
public interface CinemaClient {
    @GetMapping("/{cinemaId}")
    public ApiRes<CinemaRes> getDetailCinema(@PathVariable Long cinemaId);
}
