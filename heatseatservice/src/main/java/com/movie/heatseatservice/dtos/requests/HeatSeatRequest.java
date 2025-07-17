package com.movie.heatseatservice.dtos.requests;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatSeatRequest {
    private Long showTimeId;
    private Set<Long> seatIds;
}