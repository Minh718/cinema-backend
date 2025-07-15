package com.movie.seatservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ApiRes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiRes<T> {

    private int code;
    private String message;
    private T result;
}