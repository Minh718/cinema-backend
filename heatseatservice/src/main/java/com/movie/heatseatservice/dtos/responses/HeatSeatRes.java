package com.movie.heatseatservice.dtos.responses;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatSeatRes {
    private String type; // HOLD / RELEASE
    private Long showTimeId;
    private Set<Long> seatIds;
}