package com.movie.messagingservice.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.movie.messagingservice.dtos.requests.MessageGroupReq;
import com.movie.messagingservice.dtos.responses.MessageGroupRes;
import com.movie.messagingservice.dtos.responses.MessagePrivateRes;
import com.movie.messagingservice.entities.MessageGroup;
import com.movie.messagingservice.entities.MessagePrivate;

@Mapper
public interface MessagingMapper {
    MessagingMapper INSTANCE = Mappers.getMapper(MessagingMapper.class);

    MessageGroup toMessageGroup(MessageGroupReq messageGroup);

    MessageGroupRes toMessageGroupRes(MessageGroup messageGroup);

    List<MessageGroupRes> toMessageGroupResList(List<MessageGroup> messageGroups);

    List<MessagePrivateRes> toMessagePrivateResList(List<MessagePrivate> messagePrivates);
}