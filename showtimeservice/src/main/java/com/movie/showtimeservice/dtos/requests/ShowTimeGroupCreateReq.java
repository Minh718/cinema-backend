package com.movie.showtimeservice.dtos.requests;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.movie.showtimeservice.enums.TypeShowTime;

import lombok.Data;

@Data
public class ShowTimeGroupCreateReq {
    private Long movieId;
    private Long roomId;
    private Long cinemaId;
    private LocalDate date;
    private TypeShowTime type;
    private String language;
    private Double basePrice;
    private Long duration;
}