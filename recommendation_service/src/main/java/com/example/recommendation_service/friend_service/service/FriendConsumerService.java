package com.example.recommendation_service.friend_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.friend_service.domains.events.DomainEvent;
import com.example.recommendation_service.friend_service.domains.events.FriendEventType;
import com.example.recommendation_service.repository.UserRepository;


@Service
@Transactional
public class FriendConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(FriendConsumerService.class);

    private final UserRepository repository;

    public FriendConsumerService(UserRepository repository) { 
        this.repository = repository;
    }

    public void apply(DomainEvent<Friend> event) { 
        
        switch (event.getEventType()) { 
            case FRIEND_ADDED: 
                log.info("");


            case FRIEND_REMOVED: 
                log.info("");

            default: 
                break;
        }
    }
}
