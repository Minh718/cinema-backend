package com.movie.heatseatservice.websockets;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.movie.heatseatservice.constants.TypeMessage;
import com.movie.heatseatservice.dtos.HeatSeatMessage;
import com.movie.heatseatservice.redis.RedisMessagePublisher;
import com.movie.heatseatservice.services.HeatSeatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HeatSeatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final HeatSeatService heatSeatService;
    private final RedisMessagePublisher redisMessagePublisher;

    @MessageMapping("/seats")
    public void handleHoldSeats(@Payload HeatSeatMessage message) {
        redisMessagePublisher.publish(message);
        if (message.getType().equals(TypeMessage.HEAT))
            heatSeatService.holdSeats(message);
        else if (message.getType().equals(TypeMessage.RELEASE))
            heatSeatService.releaseSeats(message);
    }

}