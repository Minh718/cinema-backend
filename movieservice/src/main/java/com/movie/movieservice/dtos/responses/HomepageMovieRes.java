package com.movie.movieservice.dtos.responses;

import java.util.List;

import com.movie.movieservice.entities.Movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomepageMovieRes {
    private List<Movie> nowShowing;
    private List<Movie> upcoming;
    private List<Long> nowShowingMovieIds;
}