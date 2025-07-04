package com.movie.authservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserInfo
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoToken {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String picture;
    private String accessToken;
    private String refreshToken;
}