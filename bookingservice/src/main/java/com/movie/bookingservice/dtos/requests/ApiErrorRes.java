package com.movie.bookingservice.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ApiErrorRes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorRes {

    private int code;
    private String message;
}