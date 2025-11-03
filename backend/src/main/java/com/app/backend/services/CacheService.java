package com.app.backend.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @CachePut(value = "tokens",key = "#token")
    public void saveToken(String token, String username){
    }
    @Cacheable(value = "tokens",key = "#token")
    public String getUsernameByToken(String token){
        return null;
    }
    @CacheEvict(value = "tokens",key = "#token")
    public void removeToken(String token){

    }
}
