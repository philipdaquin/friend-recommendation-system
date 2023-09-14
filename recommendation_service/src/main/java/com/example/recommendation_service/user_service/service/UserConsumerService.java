package com.example.recommendation_service.user_service.service;


import java.time.Instant;
import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommendation_service.friend_service.service.FriendConsumerService;
import com.example.recommendation_service.repository.UserRepository;
import com.example.recommendation_service.user_service.domains.User;
import com.example.recommendation_service.user_service.domains.events.DomainEvent;

import reactor.core.publisher.Mono;

@Service
@Transactional
public class UserConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(FriendConsumerService.class);

    private final UserRepository repository;

    public UserConsumerService(UserRepository repository) { 
        this.repository = repository;
    }

    /**
     * Apply changes to Database
     * 
     * @param event
     */
    public Mono<User> apply(DomainEvent<User> event) { 
        
        switch (event.getEventType()) { 
            case USER_ADDED: 
                log.info("Adding new User");
                return repository.save(event.getSubject());
                // break;
                

            case USER_REMOVED: 
                log.info("Removing User");
                var subject = event.getSubject();
                repository.delete(subject);
                return Mono.just(subject);
                // break;

            case USER_UPDATED: 
                log.info("Updating User Object");
                User newUser = event.getSubject();
                User curr = repository.findUserByUserId(newUser.getId());

                if (curr != null) { 
                    if (newUser.getFirstName() != null) curr.setFirstName(newUser.getFirstName());
                    if (newUser.getLastName() != null) curr.setLastName(newUser.getLastName());
                    if (newUser.getEmail() != null) curr.setEmail(newUser.getEmail());
                    curr.setLastModifiedDate(Date.from(Instant.now()));
                    return repository.save(curr);
                }
                // break;
                return Mono.just(curr);

            default: 

                return Mono.just(event.getSubject());
                // break;
        }
    }
}
