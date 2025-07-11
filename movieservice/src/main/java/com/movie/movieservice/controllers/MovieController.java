package com.movie.movieservice.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie.movieservice.dtos.requests.MovieCreateDto;
import com.movie.movieservice.dtos.responses.ApiRes;
import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.services.MovieService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ApiRes<Movie> create(@RequestBody MovieCreateDto moviedto) {
        return ApiRes.<Movie>builder().code(200).message("success").result(movieService.createMovie(moviedto)).build();
    }

    @GetMapping("/public/now-showing")
    public ApiRes<List<Movie>> getNowShowing() {
        return ApiRes.<List<Movie>>builder().code(200).result(movieService.getNowShowingMovies()).message("success").build();
    }

}