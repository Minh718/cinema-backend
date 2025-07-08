package com.movie.movieservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.movieservice.dtos.requests.MovieCreateDto;
import com.movie.movieservice.dtos.responses.ApiRes;
import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.services.MovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ApiRes<Movie> create(@RequestBody MovieCreateDto moviedto) {
        return ApiRes.<Movie>builder().code(200).message("success").result(movieService.createMovie(moviedto)).build();
    }

}