package com.movie.messagingservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.movie.messagingservice.dtos.requests.MessageGroupReq;
import com.movie.messagingservice.dtos.responses.ApiRes;
import com.movie.messagingservice.dtos.responses.ChatBoxGroupRes;
import com.movie.messagingservice.dtos.responses.ChatBoxPrivateRes;
import com.movie.messagingservice.dtos.responses.MessageGroupRes;
import com.movie.messagingservice.dtos.responses.MessagePrivateRes;
import com.movie.messagingservice.entities.ChatBoxGroup;
import com.movie.messagingservice.entities.ChatBoxPrivate;
import com.movie.messagingservice.entities.MessageGroup;
import com.movie.messagingservice.entities.MessagePrivate;
import com.movie.messagingservice.enums.ChatBoxPrivateStatus;
import com.movie.messagingservice.mappers.MessagingMapper;
import com.movie.messagingservice.repositories.ChatBoxGroupRepository;
import com.movie.messagingservice.repositories.ChatBoxPrivateRepository;
import com.movie.messagingservice.repositories.MessageGroupRepository;
import com.movie.messagingservice.repositories.MessagePrivateRepository;
import com.movie.messagingservice.websockets.services.MessagingSocketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagingService {
    private final ChatBoxGroupRepository chatBoxGroupRepository;
    private final ChatBoxPrivateRepository chatBoxPrivateRepository;
    private final MessagingSocketService messagingSocketService;
    private final MessageGroupRepository messageGroupRepository;
    private final MessagePrivateRepository messagePrivateRepository;
    private final RedisTemplate<String, String> redisTemplate;

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

    public Void trackOnlineUser(String userId) {
        redisTemplate.opsForSet().add("online_users", userId);
        return null;
    }

    public Void trackOfflineUser(String userId) {
        redisTemplate.opsForSet().remove("online_users", userId);
        return null;
    }

    public List<ChatBoxPrivateRes> getAllPrivateChatBoxesForUser(String userId) {
        List<ChatBoxPrivateRes> chatboxRess = new ArrayList<>();
        List<ChatBoxPrivate> chatboxs = chatBoxPrivateRepository
                .findAllByStatusAndId_UserId1OrId_UserId2(ChatBoxPrivateStatus.ACTIVE, userId, userId);
        if (chatboxs.size() == 0)
            return chatboxRess;
        boolean isUser1 = chatboxs.get(0).getId().getUserId1().equals(userId);
        for (ChatBoxPrivate chatbox : chatboxs) {
            ChatBoxPrivateRes chatBox = new ChatBoxPrivateRes();
            if (isUser1) {
                chatBox.setId(chatbox.getId().getUserId2());
                chatBox.setAvatar(chatbox.getUser2Avatar());
                chatBox.setName(chatbox.getUser2Name());
            } else {
                chatBox.setId(chatbox.getId().getUserId1());
                chatBox.setAvatar(chatbox.getUser1Avatar());
                chatBox.setName(chatbox.getUser1Name());
            }
            Set<String> onlineUsers = redisTemplate.opsForSet().members("online_users");
            if (onlineUsers.contains(chatBox.getId())) {
                chatBox.setOnline(true);
            }
            chatboxRess.add(chatBox);
        }
        return chatboxRess;
    }

    public List<MessageGroupRes> getMessagesOfGroup(Long groupId, int size) {
        ChatBoxGroup chatBoxGroup = chatBoxGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<MessageGroup> messageGroups = messageGroupRepository.findByChatBoxGroup(chatBoxGroup, pageable);

        return MessagingMapper.INSTANCE.toMessageGroupResList(messageGroups);

    }

    public List<MessagePrivateRes> getPrivateMessages(String userId, String friendId, int size) {
        ChatBoxPrivate chatBoxPrivate = chatBoxPrivateRepository
                .findByStatusAndId_UserId1AndId_UserId2(ChatBoxPrivateStatus.ACTIVE, userId, friendId)
                .orElseThrow(() -> new RuntimeException("Private Chat Box not found"));
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<MessagePrivate> messagePrivates = messagePrivateRepository.findByChatBoxGroup(chatBoxPrivate, pageable);
        return MessagingMapper.INSTANCE.toMessagePrivateResList(messagePrivates);

    }
}