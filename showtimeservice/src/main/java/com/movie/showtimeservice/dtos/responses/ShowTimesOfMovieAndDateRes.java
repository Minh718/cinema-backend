package com.movie.showtimeservice.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public class ShowTimesOfMovieAndDateRes {
    List<ShowTimeRes> dubShows;
    List<ShowTimeRes> subShows;
}