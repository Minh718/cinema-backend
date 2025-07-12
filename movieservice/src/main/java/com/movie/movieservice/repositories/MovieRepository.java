package com.movie.movieservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.movieservice.entities.Movie;
import com.movie.movieservice.enums.MovieStatus;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenreContainingIgnoreCase(String genre);

    List<Movie> findMoviesByStatus(MovieStatus status);
}