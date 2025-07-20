package com.movie.cinemaservice.dtos.responses;

import java.util.List;

import com.movie.cinemaservice.entities.Seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDetailRes {
    private Long id;

    private String name;
    private Integer numberOfRows;
    private String screenType;

    private Integer seatsPerRow;
    private List<SeatRes> seats;
}