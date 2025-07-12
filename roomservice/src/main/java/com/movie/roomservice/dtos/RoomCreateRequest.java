package com.movie.roomservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequest {

    private String name;

    private String type;

    private Boolean active;

    private Integer numberOfRows;

    private Integer seatsPerRow;

    private String location;

    private String screenType;

}
