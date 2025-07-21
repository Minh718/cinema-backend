package com.movie.showtimeservice.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowTimesOfMovie {
    private String language;
    private String subtitle;
    private Double basePrice;

    private Long movieId;
    private Long roomId;
    private Long cinemaId;
    private List<TimeShowTimeRes> subShows;
    private List<TimeShowTimeRes> dubShows;
}
