package com.movie.bookingservice.dtos.requests;

import java.util.List;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private Long showTimeId;
    private Long roomId;
    private List<Long> seatIds;
}