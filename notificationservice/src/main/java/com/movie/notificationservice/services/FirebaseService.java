package com.movie.notificationservice.services;

import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public boolean sendPush(Long userId, String title, String message) {
        System.out.printf("🔥 PUSH to user %d: [%s] %s%n", userId, title, message);
        return true;
    }
}