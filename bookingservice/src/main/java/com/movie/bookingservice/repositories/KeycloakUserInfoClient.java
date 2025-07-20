package com.movie.bookingservice.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.movie.bookingservice.dtos.responses.UserInfoResponse;

@FeignClient(name = "keycloakUserInfoClient", url = "${keycloak.url}")
public interface KeycloakUserInfoClient {

    @GetMapping("/realms/{realm}/protocol/openid-connect/userinfo")
    UserInfoResponse getUserInfo(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("realm") String realm);
}