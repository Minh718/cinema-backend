package com.movie.messagingservice.websockets.schedules;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.movie.messagingservice.dtos.responses.GroupOnlineInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OnlineUserBroadcasterRedis {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 5000)
    public void broadcastOnlineCounts() {
        List<GroupOnlineInfo> payload = new ArrayList<>();

        redisTemplate.execute((RedisCallback<Object>) connection -> {
            ScanOptions options = ScanOptions.scanOptions().match("group:*:online_users").build();
            try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                while (cursor.hasNext()) {
                    String redisKey = new String(cursor.next());
                    String groupIdStr = extractGroupId(redisKey);
                    if (groupIdStr != null) {
                        try {
                            Long groupId = Long.parseLong(groupIdStr);
                            Long count = redisTemplate.opsForSet().size(redisKey);
                            if (count != null) {
                                payload.add(new GroupOnlineInfo(groupId, count.intValue()));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        if (!payload.isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/groups/online", payload);
        }
    }

    private String extractGroupId(String redisKey) {
        if (redisKey.startsWith("group:") && redisKey.endsWith(":online_users")) {
            return redisKey.substring(6, redisKey.length() - 14); // remove "group:" and ":online_users"
        }
        return null;
    }
}
