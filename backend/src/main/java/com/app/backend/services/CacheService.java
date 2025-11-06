package com.app.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisTemplate<String,String> redisTemplate;
    private static final String PREFIX="access-token:";

    public void saveToken(String token,String username){
        redisTemplate.opsForValue().set(PREFIX+token,username, Duration.ofMinutes(15));
    }
    public String getUsernameByToken(String token){
        return redisTemplate.opsForValue().get(PREFIX+token);
    }
    public void removeToken(String token){
        redisTemplate.delete(PREFIX+token);
    }
    public boolean exists(String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX+token));
    }
    public void removeTokenByUsername(String username){
        var keys=redisTemplate.keys(PREFIX+"*");
        for (String key : keys) {
            String value = redisTemplate.opsForValue().get(key);
            if (username.equals(value)) {
                redisTemplate.delete(value);
            }
        }
    }
}
