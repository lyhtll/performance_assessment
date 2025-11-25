package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private static final String KEY_PREFIX = "user:";
    private static final long TTL_MINUTES = 30L;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void save(User user) {
        try {
            String key = getKey(user.getName());
            String value = objectMapper.writeValueAsString(user);
            redisTemplate.opsForValue().set(key, value, TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user to cache", e);
        }
    }

    public User findByUsername(String username) {
        try {
            String key = getKey(username);
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            return objectMapper.readValue(value, User.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(String username) {
        redisTemplate.delete(getKey(username));
    }

    private String getKey(String username) {
        return KEY_PREFIX + username;
    }
}

