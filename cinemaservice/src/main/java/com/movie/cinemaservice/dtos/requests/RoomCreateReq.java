package com.movie.cinemaservice.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateReq {

    private String name;

    private String type;

    private Boolean active;

    private Integer numberOfRows;

    private Integer seatsPerRow;

    private String location;

    private String screenType;

}
