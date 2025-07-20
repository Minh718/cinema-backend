package com.movie.bookingservice.dtos.requests;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingInfoReq {
    private Long bookingId;
    private String phone;
    private String email;
    private String customerName;
    private String movieTitle;
    private String cinemaName;
    private String showTime;
    private String seatNumbers;
    private Double totalPrice;
}