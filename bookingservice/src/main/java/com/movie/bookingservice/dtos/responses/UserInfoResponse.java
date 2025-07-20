package com.movie.bookingservice.dtos.responses;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String sub;
    private String email;
    private String preferred_username;
    private String phone_number;
    private String name;
    // Add more fields if needed
}