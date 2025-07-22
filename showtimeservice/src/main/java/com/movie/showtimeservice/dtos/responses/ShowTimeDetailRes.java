package com.movie.showtimeservice.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.movie.showtimeservice.enums.TypeShowTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ShowTimeDetailRes {

    private Long id;

    private LocalDate date;
    private Long movieId;
    private Long roomId;
    private Long cinemaId;
    private TypeShowTime type;

    private String language;
    private Double basePrice;
    private Long duration;
    @JsonFormat(pattern = "HH:mm")

    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")

    private LocalTime endTime;

    public ShowTimeDetailRes(Long id, LocalDate date, Long movieId, Long roomId, Long cinemaId,
            TypeShowTime type, String language, Double basePrice, Long duration,
            LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.date = date;
        this.movieId = movieId;
        this.roomId = roomId;
        this.cinemaId = cinemaId;
        this.type = type;
        this.language = language;
        this.basePrice = basePrice;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}