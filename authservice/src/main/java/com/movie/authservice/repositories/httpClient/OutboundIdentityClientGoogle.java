package com.movie.authservice.repositories.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.movie.authservice.dtos.request.GetTokenGoogleReq;
import com.movie.authservice.dtos.response.GetTokenGoogleRes;

import feign.QueryMap;

@FeignClient(name = "outbound-google", url = "https://oauth2.googleapis.com")
public interface OutboundIdentityClientGoogle {

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GetTokenGoogleRes getToken(@QueryMap GetTokenGoogleReq getTokenGoogleReq);

}