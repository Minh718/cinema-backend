package com.movie.movieservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.movieservice.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenreContainingIgnoreCase(String genre);

    List<Movie> findMoviesByStatus(Boolean status);
}