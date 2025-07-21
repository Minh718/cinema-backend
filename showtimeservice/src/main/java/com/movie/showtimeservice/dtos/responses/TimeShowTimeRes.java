package com.movie.showtimeservice.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeShowTimeRes {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
