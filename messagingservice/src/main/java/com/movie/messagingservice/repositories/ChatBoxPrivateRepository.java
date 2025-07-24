package com.movie.messagingservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.messagingservice.entities.ChatBoxPrivate;
import com.movie.messagingservice.entities.ChatBoxPrivateKey;

@Repository
public interface ChatBoxPrivateRepository extends JpaRepository<ChatBoxPrivate, ChatBoxPrivateKey> {

}