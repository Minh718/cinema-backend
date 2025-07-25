package com.movie.bookingservice.dtos;

import java.util.Set;

import com.movie.bookingservice.dtos.requests.SeatReq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusTracker {
    private boolean seatLocked;
    private boolean paymentReady;
    private String paymentUrl;
    private Long bookingId;
}