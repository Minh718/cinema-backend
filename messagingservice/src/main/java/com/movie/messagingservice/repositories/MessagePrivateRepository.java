package com.movie.messagingservice.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.messagingservice.entities.ChatBoxPrivate;
import com.movie.messagingservice.entities.MessagePrivate;

@Repository
public interface MessagePrivateRepository extends JpaRepository<MessagePrivate, Long> {
    List<MessagePrivate> findByChatBoxGroup(ChatBoxPrivate chatBoxPrivate, Pageable pageable);
}