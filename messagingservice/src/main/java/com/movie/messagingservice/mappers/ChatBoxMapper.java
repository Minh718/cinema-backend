package com.movie.messagingservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.messagingservice.dtos.requests.ChatBoxPrivateCreateReq;
import com.movie.messagingservice.entities.ChatBoxPrivate;

@Mapper
public interface ChatBoxMapper {
    ChatBoxMapper INSTANCE = Mappers.getMapper(ChatBoxMapper.class);

    ChatBoxPrivate toChatBoxPrivate(ChatBoxPrivateCreateReq chatBoxPrivateCreateReq);
}