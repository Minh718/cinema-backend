package com.movie.cinemaservice.dtos.requests;

import lombok.Data;

@Data
public class CinemaCreateReq {
    private String name;
    private String brand;
    private String description;
    private String city;
    private String district;
    private String address;
    private String phoneNumber;
    private String email;
    private String mapUrl;
    private Double latitude;
    private Double longitude;
    private String managerId;
}