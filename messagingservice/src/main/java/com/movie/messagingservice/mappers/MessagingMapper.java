package com.movie.messagingservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.messagingservice.dtos.requests.MessageGroupReq;
import com.movie.messagingservice.dtos.responses.MessageGroupRes;
import com.movie.messagingservice.entities.MessageGroup;

@Mapper
public interface MessagingMapper {
    MessagingMapper INSTANCE = Mappers.getMapper(MessagingMapper.class);

    MessageGroup toMessageGroup(MessageGroupReq messageGroup);

    MessageGroupRes toMessageGroupRes(MessageGroup messageGroup);
}