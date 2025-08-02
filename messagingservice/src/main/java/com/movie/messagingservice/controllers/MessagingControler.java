package com.movie.messagingservice.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.messagingservice.dtos.responses.ApiRes;
import com.movie.messagingservice.dtos.responses.ChatBoxGroupRes;
import com.movie.messagingservice.dtos.responses.ChatBoxPrivateRes;
import com.movie.messagingservice.dtos.responses.MessageGroupRes;
import com.movie.messagingservice.dtos.responses.MessagePrivateRes;
import com.movie.messagingservice.services.MessagingService;

import jakarta.ws.rs.HeaderParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messaging")
@RequiredArgsConstructor
public class MessagingControler {

    private final MessagingService messagingService;

    @GetMapping("/chatboxs")
    public ApiRes<List<ChatBoxPrivateRes>> getAllPrivateChatBoxesForUser(@HeaderParam("X-User-Id") String userId) {
        return ApiRes.<List<ChatBoxPrivateRes>>builder().code(200).message("Success")
                .result(messagingService.getAllPrivateChatBoxesForUser(userId)).build();
    }

    @GetMapping("/user-online")
    public ApiRes<Void> trackOnlineUser(@HeaderParam("X-User-Id") String userId) {
        return ApiRes.<Void>builder().code(200).message("Success")
                .result(messagingService.trackOnlineUser(userId)).build();
    }

    @GetMapping("/user-offline")
    public ApiRes<Void> trackOfflineUser(@HeaderParam("X-User-Id") String userId) {
        return ApiRes.<Void>builder().code(200).message("Success")
                .result(messagingService.trackOfflineUser(userId)).build();
    }

    @GetMapping("/group/messages")
    public ApiRes<List<MessageGroupRes>> getGroupMessages(@RequestParam Long id,
            @RequestParam(defaultValue = "10") int size) {
        return ApiRes.<List<MessageGroupRes>>builder().code(200).message("Success")
                .result(messagingService.getMessagesOfGroup(id, size)).build();
    }

    @GetMapping("/private/messages")
    public ApiRes<List<MessagePrivateRes>> getPrivateMessages(@RequestParam String friendId,
            @HeaderParam("X-User-Id") String idUser,
            @RequestParam(defaultValue = "10") int size) {
        return ApiRes.<List<MessagePrivateRes>>builder().code(200).message("Success")
                .result(messagingService.getPrivateMessages(idUser, friendId, size)).build();
    }
}
