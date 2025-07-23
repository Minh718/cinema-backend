package com.movie.movieservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.movie.movieservice.dtos.requests.MovieRequestDto;
import com.movie.movieservice.dtos.responses.HomepageMovieRes;
import com.movie.movieservice.dtos.responses.MovieDetailRes;
import com.movie.movieservice.dtos.responses.MovieNameRes;
import com.movie.movieservice.dtos.responses.MovieRes;
import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.enums.MovieStatus;
import com.movie.movieservice.exceptions.CustomException;
import com.movie.movieservice.exceptions.ErrorCode;
import com.movie.movieservice.mappers.MovieMapper;
import com.movie.movieservice.repositories.MovieRepository;
import com.movie.movieservice.repositories.httpClients.ShowTimeClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowTimeClient showTimeClient;

    public Movie createMovie(MovieRequestDto moviedDto) {
        Movie movie = MovieMapper.INSTANCE.toMovie(moviedDto);
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getNowShowingMovies() {
        return movieRepository.findMoviesByStatus(MovieStatus.NOW_SHOWING);
    }

    public List<Movie> getComingSoonMovies() {
        return movieRepository.findMoviesByStatus(MovieStatus.UPCOMING);
    }

    public List<Long> getMovieIdsHaveShowTime(Long cinemaId) {
        return showTimeClient.getNowShowingMovieIdsByCinema(cinemaId);
    }

    public MovieDetailRes getMovieDetail(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MOVIE_DONT_EXISTED));
        MovieDetailRes movieRes = MovieMapper.INSTANCE.toMovieDetailRes(movie);
        return movieRes;
    }

    public MovieNameRes getMovieName(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MOVIE_DONT_EXISTED));
        MovieNameRes movieRes = MovieMapper.INSTANCE.toMovieNameRes(movie);
        return movieRes;
    }

    public Movie updateExistingMovie(Long id, MovieRequestDto moviedDto) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            Movie existingMovie = movie.get();
            MovieMapper.INSTANCE.updateMovieFromDto(moviedDto, existingMovie);
            return movieRepository.save(existingMovie);
        }
        return movie.orElse(null);
    }

    public Movie deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_DONT_EXISTED));

        movieRepository.delete(movie);

        return movie;
    }

    public Page<Movie> getMoviesForAdminTable(int page, int size, String sortBy, String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }
        return movieRepository.findAll(pageable);
    }

    @Cacheable(value = "homepageMovies", key = "#cinemaId")
    public HomepageMovieRes getHomepageMovies(Long cinemaId) {
        List<Movie> nowShowing = getNowShowingMovies();

        List<Movie> upcoming = getComingSoonMovies();

        return HomepageMovieRes.builder()
                .nowShowing(nowShowing)
                .upcoming(upcoming)
                .build();
    }

    public Movie updateMovieStatus(Long id, MovieStatus status) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            Movie existingMovie = movie.get();
            existingMovie.setStatus(status);
            return movieRepository.save(existingMovie);
        }
        return movie.orElse(null);
    }

    public Page<MovieRes> getAllMovies(int page, int size, String sortBy,
            String orderBy) {
        Sort.Direction sortDirection = orderBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Movie> movies = movieRepository.findAll(pageable);
        return movies.map(MovieMapper.INSTANCE::toMovieRes);
    }
}