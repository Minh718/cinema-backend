package com.movie.cinemaservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(1000, "Bad request", HttpStatus.BAD_REQUEST),
    CINEMA_NOT_FOUND(1001, "Cinema not found", HttpStatus.BAD_REQUEST),
    ROOM_NOT_FOUND(1002, "Room not found", HttpStatus.BAD_REQUEST),
    QUANTITY_NOT_ENOUGH(1047, "Quantity not enough", HttpStatus.BAD_REQUEST);

    ErrorCode(

            int code, String message,
            HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}