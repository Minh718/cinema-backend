package com.movie.heatseatservice.redis;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.movie.heatseatservice.constants.TypeMessage;
import com.movie.heatseatservice.dtos.HeatSeatMessage;
import com.movie.heatseatservice.services.HeatSeatService;

@Component
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private HeatSeatService heatSeatService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            HeatSeatMessage msg = (HeatSeatMessage) ois.readObject();
            if (msg.getType().equals(TypeMessage.HEAT))
                heatSeatService.holdSeats(msg);
            else if (msg.getType().equals(TypeMessage.RELEASE))
                heatSeatService.releaseSeats(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}