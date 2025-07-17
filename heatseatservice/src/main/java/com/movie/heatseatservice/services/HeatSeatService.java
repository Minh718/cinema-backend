package com.movie.heatseatservice.services;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.movie.heatseatservice.dtos.requests.HeatSeatRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeatSeatService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final SocketService socketService;

    public void holdSeats(HeatSeatRequest request) {
        String redisKey = "showtime:" + request.getShowTimeId() + ":heat-seats";

        redisTemplate.opsForSet().add(redisKey, request.getSeatIds().toArray());

        socketService.broadcastToShowTimeBooking(request, "HOLD");
    }

    public void releaseSeats(HeatSeatRequest request) {
        String redisKey = "showtime:" + request.getShowTimeId() + ":heat-seats";

        redisTemplate.opsForSet().remove(redisKey, request.getSeatIds().toArray());

        socketService.broadcastToShowTimeBooking(request, "RELEASE");
    }
}