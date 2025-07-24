package com.movie.cinemaservice.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBoxGroupRes {
    private Long id;

    private String name; // optional for group chats
    private LocalDateTime createdAt;
}
