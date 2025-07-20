package com.movie.bookingservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaRes {

    private Long id;

    private String name; // e.g., CGV Aeon Mall Tân Phú
    private String brand; // e.g., CGV, Lotte, BHD
    private String description; // Optional details about this cinema
    private Double latitude; // e.g., 10.762622
    private Double longitude; // e.g., 106.660172
}