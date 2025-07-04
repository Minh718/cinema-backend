package com.movie.authservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.authservice.dtos.response.ApiRes;
import com.movie.authservice.dtos.response.UserInfoToken;
import com.movie.authservice.services.AuthService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin/google")
    public ApiRes<UserInfoToken> userSigninByGoogle(
            @RequestParam() String code) {
        return ApiRes.<UserInfoToken>builder()
                .code(1000)
                .message("Signin by google successfully")
                .result(authService.userSigninByGoogle(code))
                .build();
    }
}