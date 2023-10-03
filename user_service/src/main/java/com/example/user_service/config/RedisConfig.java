package com.example.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import com.example.user_service.domains.User;

/**
 *  Spring Reactive Redis 
 *  
 *  - Time To Live: 60L
 *  - Eviction Policy
 * 
 * 
 * 
 */
@Configuration
// @EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {
    
    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.host}")
    private String redisHost;

    @Bean
    public ReactiveRedisConnectionFactory connectionFactory() { 
        return new  LettuceConnectionFactory("localhost", 6379);
    }
    @Bean
    public ReactiveRedisTemplate<String, User> reactiveRedis(ReactiveRedisConnectionFactory factory) {
        return new ReactiveRedisTemplate<>(
            factory, 
            RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(User.class))
        );
    }

}
