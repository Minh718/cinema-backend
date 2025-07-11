package com.movie.movieservice.services;

import org.springframework.stereotype.Service;

import com.movie.movieservice.dtos.requests.MovieCreateDto;
import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.mappers.MovieMapper;
import com.movie.movieservice.repositories.MovieRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie createMovie(MovieCreateDto moviedDto) {
        Movie movie = MovieMapper.INSTANCE.toMovie(moviedDto);
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getNowShowingMovies() {
        return movieRepository.findMoviesByStatus(true);
    }
}