package com.movie.heatseatservice.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.movie.heatseatservice.dtos.HeatSeatMessage;

@Service
public class RedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, HeatSeatMessage> redisTemplate;

    @Autowired
    private ChannelTopic topic;

    public void publish(HeatSeatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}