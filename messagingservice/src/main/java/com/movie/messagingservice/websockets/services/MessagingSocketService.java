package com.movie.messagingservice.websockets.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.movie.messagingservice.dtos.responses.ChatBoxPrivateMessageRes;
import com.movie.messagingservice.dtos.responses.MessageGroupRes;
import com.movie.messagingservice.dtos.responses.MessagePrivateRes;
import com.movie.messagingservice.entities.ChatBoxPrivate;
import com.movie.messagingservice.entities.MessageGroup;
import com.movie.messagingservice.mappers.MessagingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagingSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendGroupMessage(MessageGroup messageGroup) {
        MessageGroupRes message = MessagingMapper.INSTANCE.toMessageGroupRes(messageGroup);
        simpMessagingTemplate.convertAndSend(
                "/topic/groups/" + messageGroup.getChatBoxGroup().getId(),
                message);
    }

    public void sendPrivateMessage(MessagePrivateRes message, ChatBoxPrivateMessageRes chatBox, String to) {

        // notify to chat sidebar
        simpMessagingTemplate.convertAndSendToUser(to, "/specific/chat-notification", chatBox);

        // notify to chatArea
        simpMessagingTemplate.convertAndSendToUser(to, "/specific/chat", message);
    }
}
