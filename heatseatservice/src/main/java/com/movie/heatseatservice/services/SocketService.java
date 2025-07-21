package com.movie.heatseatservice.services;

import java.time.Duration;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.movie.heatseatservice.dtos.HeatSeatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void broadcastToShowTimeBooking(HeatSeatMessage message) {
        // Broadcast to WebSocket subscribers
        simpMessagingTemplate.convertAndSend(
                "/topic/seats/" + message.getShowTimeId(),
                message);
    }

}