package com.movie.showtimeservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.movie.showtimeservice.dtos.responses.ApiErrorRes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    ResponseEntity<ApiErrorRes> handlingCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiErrorRes apiErrorRes = new ApiErrorRes();

        apiErrorRes.setCode(errorCode.getCode());
        if (errorCode.getMessage().equals("BAD_REQUEST"))
            apiErrorRes.setMessage(exception.getMessage());
        else
            apiErrorRes.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiErrorRes);
    }

}