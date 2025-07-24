package com.movie.messagingservice.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ApiRes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageGroupRes {
    private String id;
    private String message;
    private String sender;
    private String name;
    private String avatar;
    private LocalDateTime createdAt;
}