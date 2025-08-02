package com.movie.messagingservice.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.messagingservice.entities.ChatBoxGroup;
import com.movie.messagingservice.entities.MessageGroup;

@Repository
public interface MessageGroupRepository extends JpaRepository<MessageGroup, Long> {
    List<MessageGroup> findByChatBoxGroup(ChatBoxGroup chatBoxGroup, Pageable pageable);
}