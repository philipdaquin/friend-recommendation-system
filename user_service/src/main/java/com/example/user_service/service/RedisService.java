package com.example.user_service.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.user_service.domains.User;

import reactor.core.publisher.Mono;

@Service
public class RedisService {
    
    private final ReactiveRedisTemplate<String, User> template;

    public RedisService(ReactiveRedisTemplate<String, User> template) { 
        this.template = template;
    }

    // @CachePut(cacheNames = "user", key = "#user.id")
    public Mono<Boolean> put(String key, User user) { 
        return template.opsForValue().set(key, user);
    }

    // @Cacheable(cacheNames = "user", key="#key")
    public Mono<User> get(String key) { 
        return template.opsForValue().get(key);
    }

    // @CacheEvict(cacheNames = "user", key = "#key")
    public Mono<Boolean> delete(String key) { 
        return template.opsForValue().delete(key);
    }

}
