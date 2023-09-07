package com.example.recommendation_service.user_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommendation_service.friend_service.service.FriendConsumerService;
import com.example.recommendation_service.repository.UserRepository;
import com.example.recommendation_service.user_service.domains.User;
import com.example.recommendation_service.user_service.domains.events.DomainEvent;

@Service
@Transactional
public class UserConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(FriendConsumerService.class);

    private final UserRepository repository;

    public UserConsumerService(UserRepository repository) { 
        this.repository = repository;
    }

    public void apply(DomainEvent<User> event) { 
        
        switch (event.getEventType()) { 
            case USER_ADDED: 
                log.info("");


            case USER_REMOVED: 
                log.info("");

            case USER_UPDATED: 
                log.info("");


            default: 
                break;
        }
    }
}
