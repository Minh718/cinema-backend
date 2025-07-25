package com.movie.messagingservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.messagingservice.entities.ChatBoxGroup;

@Repository
public interface ChatBoxGroupRepository extends JpaRepository<ChatBoxGroup, Long> {

}