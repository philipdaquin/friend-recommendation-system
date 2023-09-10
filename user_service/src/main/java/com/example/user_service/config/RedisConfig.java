package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
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
    
    @Bean
    public ReactiveRedisTemplate<String, User> reactiveRedis(ReactiveRedisConnectionFactory factory) {

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        StringRedisSerializer stringRedisSerializer = StringRedisSerializer.UTF_8;
        GenericToStringSerializer<User> longToStringSerializer = new GenericToStringSerializer<>(User.class);
        
        var context = RedisSerializationContext.<String, User>newSerializationContext(jdkSerializationRedisSerializer)
            .key(stringRedisSerializer)
            .value(longToStringSerializer)
            .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
