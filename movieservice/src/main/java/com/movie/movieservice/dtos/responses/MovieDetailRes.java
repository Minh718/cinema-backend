package com.movie.movieservice.dtos.responses;

import java.time.LocalDate;
import java.util.Set;

import com.movie.movieservice.entities.Genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetailRes {
    private Long id;

    private String title;
    private String description;
    private Integer duration;
    private String director;
    private String language;
    private Set<Genre> genre;
    private String thumbnailUrl;
    private LocalDate releaseDate;
    private String backdropPath;
    private String posterPath;

}