package com.movie.showtimeservice.dtos.requests;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AutoAssignRequest {
    private Long movieId;
    private Long roomId;
    private Long cinemaId;
    private LocalDate date;
    private int movieDuration; // in minutes, temporarily passed manually
}