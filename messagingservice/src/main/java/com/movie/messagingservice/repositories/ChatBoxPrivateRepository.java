package com.movie.messagingservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.messagingservice.entities.ChatBoxPrivate;
import com.movie.messagingservice.entities.ChatBoxPrivateKey;
import com.movie.messagingservice.enums.ChatBoxPrivateStatus;

@Repository
public interface ChatBoxPrivateRepository extends JpaRepository<ChatBoxPrivate, ChatBoxPrivateKey> {
    List<ChatBoxPrivate> findAllByStatusAndId_UserId1OrId_UserId2(ChatBoxPrivateStatus status, String userId1,
            String userId2);

    Optional<ChatBoxPrivate> findByStatusAndId_UserId1AndId_UserId2(ChatBoxPrivateStatus status, String userId1,
            String userId2);
}