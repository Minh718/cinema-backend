package com.movie.cinemaservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRes {
    private Long id;

    private int row;
    private String code;
    private String status = "available";
}