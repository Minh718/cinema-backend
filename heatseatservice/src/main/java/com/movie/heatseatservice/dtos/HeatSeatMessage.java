package com.movie.heatseatservice.dtos;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatSeatMessage {
    private String type; // HOLD / RELEASE
    private Long showTimeId;
    private Set<Long> seatIds;
    private String senderId;
}