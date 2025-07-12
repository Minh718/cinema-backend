package com.movie.movieservice.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.movieservice.dtos.requests.MovieRequestDto;
import com.movie.movieservice.dtos.responses.ApiMetaRes;
import com.movie.movieservice.dtos.responses.ApiRes;
import com.movie.movieservice.dtos.responses.MetadataDTO;
import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.services.MovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/admin")
    public ApiRes<Movie> create(@RequestBody MovieRequestDto moviedto) {
        return ApiRes.<Movie>builder().code(200).message("success").result(movieService.createMovie(moviedto)).build();
    }

    @PutMapping("/admin/{id}")
    public ApiRes<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody MovieRequestDto moviedto) {
        return ApiRes.<Movie>builder().code(200).message("success")
                .result(movieService.updateExistingMovie(id, moviedto)).build();
    }

    @DeleteMapping("/admin/{id}")
    public ApiRes<Movie> deleteMovie(@RequestBody MovieRequestDto moviedto) {
        return ApiRes.<Movie>builder().code(200).message("success").result(movieService.createMovie(moviedto)).build();
    }

    @GetMapping("/public/now-showing")
    public ApiRes<List<Movie>> getNowShowing() {
        return ApiRes.<List<Movie>>builder().code(200).result(movieService.getNowShowingMovies()).message("success")
                .build();
    }

    @GetMapping("/admin/all")
    public ApiMetaRes<List<Movie>> searchMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy) {
        Page<Movie> moviePage = movieService.getMoviesForAdminTable(size, page, sortBy,
                orderBy);
        MetadataDTO metadata = new MetadataDTO(
                moviePage.getTotalElements(),
                moviePage.getTotalPages(),
                moviePage.getNumber(),
                moviePage.getSize());
        return ApiMetaRes.<List<Movie>>builder().code(1000).message("Successfully")
                .result(moviePage.getContent()).metadata(metadata).build();
    }

    @GetMapping("/public/coming-soon")
    public ApiRes<List<Movie>> getComingSoon() {
        return ApiRes.<List<Movie>>builder().code(200).result(movieService.getComingSoonMovies()).message("success")
                .build();
    }

    @GetMapping("/public/{id}")
    public ApiRes<Movie> getMovieDetail(@PathVariable("id") Long id) {
        return ApiRes.<Movie>builder().code(200).result(movieService.getMovieDetail(id)).message("success").build();
    }
}