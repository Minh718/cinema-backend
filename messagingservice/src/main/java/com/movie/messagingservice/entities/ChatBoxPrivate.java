package com.movie.messagingservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL)
    private List<Message> messages;

    private LocalDateTime lastMessageAt;
}