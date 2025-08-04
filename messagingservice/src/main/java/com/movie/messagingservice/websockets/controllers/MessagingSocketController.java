package com.movie.messagingservice.websockets.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.movie.messagingservice.dtos.requests.MessageGroupReq;
import com.movie.messagingservice.dtos.requests.MessagePrivateReq;
import com.movie.messagingservice.services.MessagingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MessagingSocketController {
    private final MessagingService messagingService;

    @MessageMapping("/group")
    public void handleSendGroupMessage(@Payload MessageGroupReq groupMessage) {
        messagingService.handleSendGroupMessage(groupMessage);
    }

    @MessageMapping("/private")
    public void handleSendPrivateMessage(@Payload MessagePrivateReq message) {
        messagingService.handleSendPrivateMessage(message);
    }
}