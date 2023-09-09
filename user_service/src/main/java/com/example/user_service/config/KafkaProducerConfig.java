package com.example.user_service.config;

import java.util.Map;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;

import reactor.kafka.sender.SenderOptions;

@EnableKafka
@Configuration
public class KafkaProducerConfig {
    
    @Bean
    public ReactiveKafkaProducerTemplate<String, DomainEvent<User>> reactiveProducer(KafkaProperties properties) { 
        Map<String, Object> config = properties.buildProducerProperties();
        
        return new ReactiveKafkaProducerTemplate<String, DomainEvent<User>>(SenderOptions.create(config));
    }
    
}
