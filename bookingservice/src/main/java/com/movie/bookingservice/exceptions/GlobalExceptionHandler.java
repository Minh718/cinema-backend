package com.movie.bookingservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.movie.bookingservice.dtos.requests.ApiErrorRes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiErrorRes> handlingRuntimeException(RuntimeException exception) {
        ApiErrorRes apiErrorRes = new ApiErrorRes();
        System.out.println(exception);
        apiErrorRes.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiErrorRes.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiErrorRes);
    }

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