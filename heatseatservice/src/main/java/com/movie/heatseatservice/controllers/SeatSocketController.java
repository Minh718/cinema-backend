package com.movie.heatseatservice.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.movie.heatseatservice.constants.TypeMessage;
import com.movie.heatseatservice.dtos.HeatSeatMessage;
import com.movie.heatseatservice.services.HeatSeatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SeatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final HeatSeatService heatSeatService;

    @MessageMapping("/seats")
    public void handleHoldSeats(@Payload HeatSeatMessage message) {
        if (message.getType().equals(TypeMessage.HEAT))
            heatSeatService.holdSeats(message);
        else if (message.getType().equals(TypeMessage.RELEASE))
            heatSeatService.releaseSeats(message);
    }

}