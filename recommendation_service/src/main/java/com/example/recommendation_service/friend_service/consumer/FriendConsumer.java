package com.example.recommendation_service.friend_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.friend_service.domains.events.DomainEvent;
import com.example.recommendation_service.friend_service.service.FriendConsumerService;

@Component
public class FriendConsumer {

    private static final Logger log = LoggerFactory.getLogger(FriendConsumer.class);
    
    private static final String topic = "friend";

    private final FriendConsumerService service;

    public FriendConsumer(FriendConsumerService service) { 
        this.service = service;
    } 


    @KafkaListener(topics = topic, groupId = "${spring.kafka.consumer.group-id}")
    public void consume(
        // ConsumerRecord<String, DomainEvent<Friend>> event, Acknowledgment ack
        @Payload DomainEvent<Friend> event
        ) {
        log.info("Friend Consumer is working");
        service.apply(event);
        // ack.acknowledge();
    }
}
