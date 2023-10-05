package com.example.recommendation_service.friend_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.friend_service.domains.events.DomainEvent;
import com.example.recommendation_service.friend_service.service.FriendConsumerService;

@Component
public class FriendConsumer {

    private static final Logger log = LoggerFactory.getLogger(FriendConsumer.class);
    
    private static final String topic = "friend";

    private final FriendConsumerService service;

    @Value("${spring.kafka.consumer.group-id}")
    private static final String consumerId = "test";

    public FriendConsumer(FriendConsumerService service) { 
        this.service = service;
    } 

    /**
     * @param event
     */
    @RetryableTopic(
        attempts = "4",
        backoff = @Backoff(delay = 1000),
        include = {ClassCastException.class},
        includeNames = "java.lang.ClassCastException"
    )
    @KafkaListener(topics = topic, groupId = consumerId)
    public void consume(
        ConsumerRecord<String, DomainEvent<Friend>> event, 
        Acknowledgment ack
        // @Payload DomainEvent<Friend> event
        ) {
        log.info("Friend Consumer is working");
        service.apply(event.value());
        ack.acknowledge();
    }
}
