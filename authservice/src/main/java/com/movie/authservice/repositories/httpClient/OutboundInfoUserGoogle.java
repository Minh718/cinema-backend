package com.movie.authservice.repositories.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.authservice.dtos.response.OutBoundInfoUser;

@FeignClient(name = "outbound-info-user", url = "https://www.googleapis.com")
public interface OutboundInfoUserGoogle {
    @GetMapping("/oauth2/v1/userinfo")
    OutBoundInfoUser getInfoUser(@RequestParam("alt") String alt,
            @RequestParam("access_token") String accessToken);

}
