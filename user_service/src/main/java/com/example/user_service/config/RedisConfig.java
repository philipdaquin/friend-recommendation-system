package com.example.user_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.user_service.domains.User;

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
    // @Bean
    // public ReactiveRedisTemplate<String, User> reactiveRedis(ReactiveRedisConnectionFactory factory) {
    //     return new ReactiveRedisTemplate<>(
    //         factory, 
    //         RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(User.class))
    //     );
    // }
    /**
	 * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and a typed
	 * {@link Jackson2JsonRedisSerializer}.
	 */
	@Bean
	public ReactiveRedisTemplate<String, User> reactiveJsonPersonRedisTemplate(
			ReactiveRedisConnectionFactory connectionFactory) {

		var serializer = new Jackson2JsonRedisSerializer<User>(User.class);
		RedisSerializationContextBuilder<String, User> builder = RedisSerializationContext
				.newSerializationContext(new StringRedisSerializer());

		var serializationContext = builder.value(serializer).build();

		return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
	}

	/**
	 * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and {@link GenericJackson2JsonRedisSerializer}.
	 */
	@Bean
	public ReactiveRedisTemplate<String, Object> reactiveJsonObjectRedisTemplate(
			ReactiveRedisConnectionFactory connectionFactory) {

		RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext
				.newSerializationContext(new StringRedisSerializer());

		var serializationContext = builder
				.value(new GenericJackson2JsonRedisSerializer("_type")).build();

		return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
	}

	/**
	 * Clear database before shut down.
	 */
	public @PreDestroy void flushTestDb() {
		connectionRedisFactory().getConnection().flushDb();
	}
}
