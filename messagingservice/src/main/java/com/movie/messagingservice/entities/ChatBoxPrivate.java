package com.movie.messagingservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.movie.messagingservice.enums.ChatBoxPrivateStatus;

@Entity
@Table(name = "chatbox_private")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBoxPrivate {

    @EmbeddedId
    private ChatBoxPrivateKey id;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL)
    private List<MessagePrivate> messages;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ChatBoxPrivateStatus status = ChatBoxPrivateStatus.UNACTIVE;

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    private LocalDateTime lastMessageAt;
    private String user1Name;
    private String user1Avatar;

    private String user2Name;
    private String user2Avatar;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}