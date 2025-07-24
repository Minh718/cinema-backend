package com.movie.messagingservice.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.messagingservice.dtos.responses.ApiRes;
import com.movie.messagingservice.dtos.responses.ChatBoxGroupRes;
import com.movie.messagingservice.services.MessagingService;

import jakarta.ws.rs.HeaderParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("internal/messaging")
@RequiredArgsConstructor
public class InternalMessagingControler {

    private final MessagingService messagingService;

    @PostMapping("/{name}")
    public ApiRes<ChatBoxGroupRes> createChatBoxGroup(@PathVariable String name) {
        return ApiRes.<ChatBoxGroupRes>builder().code(200).message("Success")
                .result(messagingService.createChatBoxGroup(name)).build();
    }

}
