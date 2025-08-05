package com.movie.messagingservice.dtos.requests;

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
public class ChatBoxPrivateCreateReq {
    private String user1Name;
    private String user1Avatar;

    private String userId2;
    private String user2Name;
    private String user2Avatar;
}