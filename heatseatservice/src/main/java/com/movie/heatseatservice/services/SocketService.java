package com.movie.heatseatservice.services;

import java.time.Duration;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.movie.heatseatservice.dtos.requests.HeatSeatRequest;
import com.movie.heatseatservice.dtos.responses.HeatSeatRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void broadcastToShowTimeBooking(HeatSeatRequest request, String typeBroadcast) {
        // Broadcast to WebSocket subscribers
        simpMessagingTemplate.convertAndSend(
                "/topic/seats/" + request.getShowTimeId(),
                new HeatSeatRes(typeBroadcast, request.getShowTimeId(), request.getSeatIds()));
    }

}