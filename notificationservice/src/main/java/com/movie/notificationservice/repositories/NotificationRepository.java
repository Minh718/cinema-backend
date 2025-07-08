package com.movie.notificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.notificationservice.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
