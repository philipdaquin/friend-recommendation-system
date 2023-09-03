package com.example.user_service.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaProducerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;

import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {

    public ReactiveKafkaProducerTemplate<String, DomainEvent<User>> reactiveProducer(KafkaProperties properties) { 
        Map<String, Object> config = properties.buildProducerProperties();
        
        return new ReactiveKafkaProducerTemplate<String, DomainEvent<User>>(SenderOptions.create(config));
    }
    
}
