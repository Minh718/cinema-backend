package com.movie.movieservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.movieservice.dtos.responses.ApiRes;
import com.movie.movieservice.dtos.responses.MovieNameRes;
import com.movie.movieservice.services.MovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("internal/movies")
@RequiredArgsConstructor
public class InternalMovieController {
    private final MovieService movieService;

    @GetMapping("/{id}")
    public ApiRes<MovieNameRes> getNameMovie(@PathVariable("id") Long id) {
        return ApiRes.<MovieNameRes>builder().code(200).result(movieService.getMovieName(id)).message("success")
                .build();
    }
}
