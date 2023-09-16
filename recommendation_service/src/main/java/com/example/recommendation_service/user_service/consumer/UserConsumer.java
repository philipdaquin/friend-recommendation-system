package com.example.recommendation_service.user_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.recommendation_service.user_service.domains.events.DomainEvent;
import com.example.recommendation_service.user_service.domains.User;
import com.example.recommendation_service.user_service.service.UserConsumerService;

@Component
public class UserConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserConsumer.class);
    
    private static final String topic = "user";

    private final UserConsumerService service;

    @Value("${spring.kafka.consumer.group-id}")
    private static final String consumerId = "test";

    public UserConsumer(UserConsumerService service) { 
        this.service = service;
    } 


    @KafkaListener(topics = topic, groupId = consumerId)
    public void consume(
        //ConsumerRecord<String, DomainEvent<User>>
        @Payload DomainEvent<User> event
        // , 
        // Acknowledgment ack
    ) {

        log.info("Friend Consumer is working");

        service.apply(event);

        // ack.acknowledge();
        
    }
}

