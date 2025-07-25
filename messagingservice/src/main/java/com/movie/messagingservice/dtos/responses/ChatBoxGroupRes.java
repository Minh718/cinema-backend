package com.movie.messagingservice.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBoxGroupRes {
    private Long id;

    private String name; // optional for group chats
    private LocalDateTime createdAt;
}
