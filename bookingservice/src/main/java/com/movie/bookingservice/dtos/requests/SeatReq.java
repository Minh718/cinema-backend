package com.movie.bookingservice.dtos.requests;

import java.util.Map;

import lombok.Data;

@Data
public class SeatReq {
    private Long id;
    private String code;
}