package com.movie.messagingservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chatbox_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBoxGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // optional for group chats
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL)
    private List<MessageGroup> messages;
    private LocalDateTime lastMessageAt;

}
