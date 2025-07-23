package com.movie.movieservice.controllers;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import com.movie.movieservice.dtos.responses.HomepageMovieRes;
import com.movie.movieservice.dtos.responses.MetadataDTO;
import com.movie.movieservice.dtos.responses.MovieDetailRes;
import com.movie.movieservice.dtos.responses.MovieRes;
import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.enums.MovieStatus;
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

    // @PutMapping("/admin/{id}/status")
    // public ApiRes<Movie> updateMovieStatus(
    // @PathVariable("id") Long id,
    // @RequestParam("status") MovieStatus status) {

    // Movie updatedMovie = movieService.updateMovieStatus(id, status);

    // ApiRes.<Movie>builder()
    // .code(200)
    // .message("Success")
    // .result(updatedMovie)
    // .build();
    // }

    @DeleteMapping("/admin/{id}")
    public ApiRes<Movie> deleteMovie(@RequestBody MovieRequestDto moviedto) {
        return ApiRes.<Movie>builder().code(200).message("success").result(movieService.createMovie(moviedto)).build();
    }

    @GetMapping("/public/now-showing")
    public ApiRes<List<Movie>> getNowShowing() {
        return ApiRes.<List<Movie>>builder().code(200).result(movieService.getNowShowingMovies()).message("success")
                .build();
    }

    @GetMapping("/public/homepage")
    public ApiRes<HomepageMovieRes> getHomepageMovies(@RequestParam Long cinemaId) {
        HomepageMovieRes HomepageMovieRes = movieService.getHomepageMovies(cinemaId);
        HomepageMovieRes.setNowShowingMovieIds(movieService.getMovieIdsHaveShowTime(cinemaId));
        return ApiRes.<HomepageMovieRes>builder().code(200).result(HomepageMovieRes)
                .message("success")
                .build();
    }

    @GetMapping("/admin/all")
    public ApiMetaRes<List<Movie>> getMoviesForAdminTable(
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
    public ApiRes<MovieDetailRes> getMovieDetail(@PathVariable("id") Long id) {
        return ApiRes.<MovieDetailRes>builder().code(200).result(movieService.getMovieDetail(id)).message("success")
                .build();
    }

    @GetMapping("/public/all")
    public ApiMetaRes<List<MovieRes>> getAllMovies(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy) {
        Page<MovieRes> moviePage = movieService.getAllMovies(page, size, sortBy, orderBy);
        MetadataDTO metadata = new MetadataDTO(
                moviePage.getTotalElements(),
                moviePage.getTotalPages(),
                moviePage.getNumber(),
                moviePage.getSize());
        return ApiMetaRes.<List<MovieRes>>builder().code(200).result(moviePage.getContent()).metadata(metadata)
                .message("success")
                .build();
    }
}