package com.example.friend_service.config;

import java.util.Map;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.events.DomainEvent;

import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {
    
    @Bean
    public ReactiveKafkaProducerTemplate<String, DomainEvent<Friend>> reactiveProducer(KafkaProperties properties) { 
        Map<String, Object> config = properties.buildProducerProperties();
        
        return new ReactiveKafkaProducerTemplate<String, DomainEvent<Friend>>(SenderOptions.create(config));
    }
    
}
