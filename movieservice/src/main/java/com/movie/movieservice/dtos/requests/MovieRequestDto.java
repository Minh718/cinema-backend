package com.movie.movieservice.dtos.requests;

import java.time.LocalDate;

import com.movie.movieservice.enums.MovieStatus;

import lombok.Data;

@Data
public class MovieRequestDto {
    private String title;
    private String description;
    private Integer duration;
    private String director;
    private String language;
    private String genre;
    private String thumbnailUrl;
    private LocalDate releaseDate;
    private LocalDate backdropPath;
    private LocalDate posterPath;
}