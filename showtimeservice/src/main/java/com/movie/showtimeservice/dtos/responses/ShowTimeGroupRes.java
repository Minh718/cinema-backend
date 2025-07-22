package com.movie.showtimeservice.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.movie.showtimeservice.enums.TypeShowTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowTimeGroupRes {

    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String language;
    private String subtitle;
    private Double basePrice;

    private Long movieId;
    private Long roomId;
    private Long cinemaId;
    private TypeShowTime type;
    List<ShowTimeRes> showTimes;
}
