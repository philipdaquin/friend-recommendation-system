package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.example.user_service.domains.User;
import com.example.user_service.domains.events.DomainEvent;

@Service
public class KafkaProducerService {
    
    private final ReactiveKafkaProducerTemplate<String, DomainEvent<User>> producer;

    @Value(value = "${FAKE_PRODUCER_DTO_TOPIC}")
    private String topic;

    public KafkaProducerService(ReactiveKafkaProducerTemplate producer) { 
        this.producer = producer;
    }

    /**
     * Emit new messages 
     * 
     * @param event
     * @return
     */
    public void send(DomainEvent<User> event) { 
        producer.send(topic, event).subscribe();
    }
}
