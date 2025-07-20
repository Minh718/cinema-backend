package com.movie.bookingservice.repositories.httpClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.bookingservice.dtos.responses.ApiRes;
import com.movie.bookingservice.dtos.responses.MovieNameRes;

import jakarta.persistence.Cacheable;

@FeignClient(name = "movie-service")
public interface MovieClient {
    @GetMapping("internal/movies/{movieId}")
    public ApiRes<MovieNameRes> getNameMovie(@PathVariable Long movieId);
}
