package com.example.friend_service.config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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
    public ReactiveKafkaProducerTemplate<String, DomainEvent<Friend>> reactiveKafkaProducerTemplate() {
        SenderOptions<String, DomainEvent<Friend>> senderOptions = SenderOptions.<String, DomainEvent<Friend>>create()
            // .producerProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091")
            .producerProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName())
            .producerProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName())
            // .producerProperty(ProducerConfig.ACKS_CONFIG, "all")
            // .producerProperty(ProducerConfig.RETRIES_CONFIG, 3)
            // .producerProperty(ProducerConfig.BATCH_SIZE_CONFIG, 16384)
            // .producerProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432)
            ;

        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }
    
}
