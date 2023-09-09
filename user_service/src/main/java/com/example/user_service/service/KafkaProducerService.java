package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;

import reactor.core.publisher.Mono;

@Service
public class KafkaProducerService {
    
    private final ReactiveKafkaProducerTemplate<String, DomainEvent<User>> producer;

    @Autowired
    public KafkaProducerService(ReactiveKafkaProducerTemplate<String, DomainEvent<User>> producerTemplate) {
        this.producer = producerTemplate;
    }

    @Value(value = "${PRODUCER_TOPIC}")
    private String topic;

 

    /**
     * Emit new messages 
     * 
     * @param event
     * @return
     */
    public Mono<Void> send(DomainEvent<User> event) { 
        return producer.send(topic, event).then();
    }
}
