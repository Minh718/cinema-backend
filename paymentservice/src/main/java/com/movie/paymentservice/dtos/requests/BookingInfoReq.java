package com.movie.paymentservice.dtos.requests;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfoReq {
    private String userId;
    private Long bookingId;
    private String phone;
    private String email;
    private String customerName;
    private String movieTitle;
    private String cinemaName;
    private String showTime;
    private String seatNumbers;
    private Double totalPrice;

    public Map<String, String> toMap() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> rawMap = mapper.convertValue(this, Map.class);

        return rawMap.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() == null ? "" : e.getValue().toString()));
    }
}
