package com.movie.cinemaservice.repositories.httpClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movie.cinemaservice.dtos.responses.ApiRes;
import com.movie.cinemaservice.dtos.responses.ChatBoxGroupRes;

@FeignClient(name = "messaging-service")
public interface MessagingClient {
    @GetMapping("internal/messaging/{name}")
    public ApiRes<ChatBoxGroupRes> createChatBoxGroup(@PathVariable String name);
}
