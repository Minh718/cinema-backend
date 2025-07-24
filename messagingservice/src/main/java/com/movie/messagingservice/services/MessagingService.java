package com.movie.messagingservice.services;

import org.springframework.stereotype.Service;

import com.movie.messagingservice.dtos.responses.ChatBoxGroupRes;
import com.movie.messagingservice.entities.ChatBoxGroup;
import com.movie.messagingservice.repositories.ChatBoxGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagingService {
    private final ChatBoxGroupRepository chatBoxGroupRepository;

    public ChatBoxGroupRes createChatBoxGroup(String name) {
        ChatBoxGroup chatBoxGroup = ChatBoxGroup.builder().name(name).build();
        chatBoxGroupRepository.save(chatBoxGroup);
        return ChatBoxGroupRes.builder().id(chatBoxGroup.getId()).name(chatBoxGroup.getName())
                .createdAt(chatBoxGroup.getCreatedAt()).build();
    }
}