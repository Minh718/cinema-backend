package com.movie.bookingservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void deleteHashValue(String key, String hasKey) {
        redisTemplate.opsForHash().delete(key, hasKey);
    }

    public Long incrementHashValue(String key, String hasKey, long value) {
        return redisTemplate.opsForHash().increment(key, hasKey, value);
    }

    public Object getHashValue(String key, String hasKey) {
        return redisTemplate.opsForHash().get(key, hasKey);
    }

    public void setHashValueKey(String key, String hasKey, Object value) {
        redisTemplate.opsForHash().put(key, hasKey, value);
    }

    public boolean setHashValueKeyIfAbsent(String key, String hasKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hasKey, value);
    }

    public boolean checkKeyExist(String key, String hasKey) {
        return redisTemplate.opsForHash().hasKey(key, hasKey);
    }

    public void setKeyinMinutes(String key, Object value, int MINUTES) {
        redisTemplate.opsForValue().set(key, value, MINUTES, TimeUnit.MINUTES);
    }

    public void setKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setKeys(Map<String, Long> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    public void setKeyInDate(String key, Object value, int days) {
        redisTemplate.opsForValue().set(key, value, days, TimeUnit.DAYS);
    }

    public void setKeyInMilliseconds(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    public Object getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delKey(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean checkKeyExist(String key) {
        return redisTemplate.hasKey(key);
    }

    public Boolean setnxProduct(String key, Object value, long time) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public Long incrementKey(String key, long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    public List<Object> getStringArray(String key) {
        // Get the list from Redis
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public Long removeValueInArray(String key, Object value) {
        return redisTemplate.opsForList().remove(key, 0, value);
    }

    public Long getSizeArray(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void addSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }
}