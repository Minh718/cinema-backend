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
public class MovieRes {
    private Long id;
    private String title;
    private String overview;
    private Integer duration;
    private Set<Genre> genre;
    private String posterPath;
}