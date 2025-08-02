package com.movie.messagingservice.dtos.responses;

import java.time.LocalDateTime;

import com.movie.messagingservice.entities.ChatBoxPrivateKey;
import com.movie.messagingservice.enums.StatusUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBoxPrivateRes {
    private String id;
    private String name;
    private String avatar;
    private boolean online = false;
}