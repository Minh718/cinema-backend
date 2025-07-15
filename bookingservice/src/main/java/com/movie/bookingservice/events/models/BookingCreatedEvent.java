package com.movie.bookingservice.events.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingCreatedEvent {

    private Long bookingId;
    // private Long userId;

    private Long roomId;
    private Long showTimeId;

    private Set<Long> seatIds; // Requested seat IDs
    // private BigDecimal totalPrice; // Total booking amount

    // private String paymentMethod; // E.g., "MOMO", "VNPAY", "CASH"

    // private BookingInfo bookingInfo; // Extra info for payment or confirmation
}