package com.example.user_service.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;

import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {
    
    @Bean
    public ReactiveKafkaProducerTemplate<String, DomainEvent<User>> reactiveKafkaProducerTemplate() {
        SenderOptions<String, DomainEvent<User>> senderOptions = SenderOptions.<String, DomainEvent<User>>create()
            .producerProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            .producerProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName())
            .producerProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName())
            .producerProperty(ProducerConfig.ACKS_CONFIG, "all")
            .producerProperty(ProducerConfig.RETRIES_CONFIG, 3)
            .producerProperty(ProducerConfig.BATCH_SIZE_CONFIG, 16384)
            .producerProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }

    
}
