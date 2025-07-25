package com.movie.cinemaservice.exceptions;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.movie.cinemaservice.dtos.responses.ApiErrorRes;

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