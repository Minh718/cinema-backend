package com.movie.heatseatservice.services;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.movie.heatseatservice.dtos.HeatSeatMessage;
import com.movie.heatseatservice.redis.RedisMessagePublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeatSeatService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final SocketService socketService;

    public void holdSeats(HeatSeatMessage message) {
        String redisKey = "showtime:" + message.getShowTimeId() + ":heat-seats";

        redisTemplate.opsForSet().add(redisKey, message.getSeatIds().toArray());

        socketService.broadcastToShowTimeBooking(message);
    }

    public void releaseSeats(HeatSeatMessage message) {
        String redisKey = "showtime:" + message.getShowTimeId() + ":heat-seats";

        redisTemplate.opsForSet().remove(redisKey, message.getSeatIds().toArray());

        socketService.broadcastToShowTimeBooking(message);
    }
}