package com.example.friend_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.events.DomainEvent;

@Service
public class KafkaProducerService {
    
    private final ReactiveKafkaProducerTemplate<String, DomainEvent<Friend>> producer;

    @Value("${PRODUCER_TOPIC}")
    private String topic = "friend";

    public KafkaProducerService(ReactiveKafkaProducerTemplate producer) { 
        this.producer = producer;
    }

    /**
     * Emit new messages 
     * 
     * @param event
     * @return
     */
    public void send(DomainEvent<Friend> event) { 
        producer.send(topic, event);
    }
}
