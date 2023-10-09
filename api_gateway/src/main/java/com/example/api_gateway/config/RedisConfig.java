package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;



import jakarta.annotation.PreDestroy;

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
    public LettuceConnectionFactory connectionRedisFactory() { 
        return new  LettuceConnectionFactory(redisHost, redisPort);
    }

	/**
	 * Clear database before shut down.
	 */
	public @PreDestroy void flushTestDb() {
		connectionRedisFactory().destroy();;
	}
}
