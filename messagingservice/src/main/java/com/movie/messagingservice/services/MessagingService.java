package com.movie.messagingservice.services;

import org.springframework.stereotype.Service;

import com.movie.messagingservice.dtos.requests.MessageGroupReq;
import com.movie.messagingservice.dtos.responses.ChatBoxGroupRes;
import com.movie.messagingservice.entities.ChatBoxGroup;
import com.movie.messagingservice.entities.MessageGroup;
import com.movie.messagingservice.mappers.MessagingMapper;
import com.movie.messagingservice.repositories.ChatBoxGroupRepository;
import com.movie.messagingservice.websockets.services.MessagingSocketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagingService {
    private final ChatBoxGroupRepository chatBoxGroupRepository;
    private final MessagingSocketService messagingSocketService;

    public ChatBoxGroupRes createChatBoxGroup(String name) {
        ChatBoxGroup chatBoxGroup = ChatBoxGroup.builder().name(name).build();
        chatBoxGroupRepository.save(chatBoxGroup);
        return ChatBoxGroupRes.builder().id(chatBoxGroup.getId()).name(chatBoxGroup.getName())
                .createdAt(chatBoxGroup.getCreatedAt()).build();
    }

    public void sendMessageToGroup(MessageGroupReq groupMessage) {
        ChatBoxGroup chatBoxGroup = chatBoxGroupRepository.findById(groupMessage.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        MessageGroup messageGroup = MessagingMapper.INSTANCE.toMessageGroup(groupMessage);
        messageGroup.setChatBoxGroup(chatBoxGroup);
        chatBoxGroup.getMessages().add(messageGroup);
        chatBoxGroupRepository.save(chatBoxGroup);
        messagingSocketService.sendMessageToGroup(messageGroup);
    }

}