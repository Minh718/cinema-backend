package com.movie.messagingservice.websockets.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.movie.messagingservice.dtos.requests.MessageGroupReq;
import com.movie.messagingservice.services.MessagingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MessagingSocketController {
    private final MessagingService messagingService;

    @MessageMapping("/groups")
    public void handleSendMessageToGroup(@Payload MessageGroupReq groupMessage) {
        messagingService.sendMessageToGroup(groupMessage);
    }
}