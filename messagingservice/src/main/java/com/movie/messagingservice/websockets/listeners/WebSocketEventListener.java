package com.movie.messagingservice.websockets.listeners;

import java.util.Set;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final RedisTemplate<String, String> redisTemplate;

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        String userId = accessor.getUser().getName(); // or custom ID
        redisTemplate.opsForValue().set("ws_session:" + sessionId, userId);
        if (destination != null && destination.startsWith("/topic/groups/")) {
            String groupId = destination.replace("/topic/groups/", "");
            redisTemplate.opsForSet().add("group:" + groupId + ":online_users", userId);
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String userId = redisTemplate.opsForValue().get("ws_session:" + sessionId);

        // Remove user from all groups they were in
        Set<String> keys = redisTemplate.keys("group:*:online_users");
        for (String key : keys) {
            redisTemplate.opsForSet().remove(key, userId);
        }
    }
}