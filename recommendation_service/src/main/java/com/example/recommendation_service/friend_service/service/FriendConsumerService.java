package com.example.recommendation_service.friend_service.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.example.recommendation_service.friend_service.domains.Friend;
import com.example.recommendation_service.friend_service.domains.events.DomainEvent;
import com.example.recommendation_service.repository.Neo4JUserRepository;

import reactor.core.publisher.Mono;


@Service
@Transactional
public class FriendConsumerService {
    
    private static final Logger log = LoggerFactory.getLogger(FriendConsumerService.class);

    private final Neo4JUserRepository repository;

    public FriendConsumerService(Neo4JUserRepository repository) { 
        this.repository = repository;
    }

    public Mono<Friend> apply(DomainEvent<Friend> event) { 
        
        Friend friend = event.getSubject();

        // Check if the USER and FRIEND exist in the database
        if (repository.findUserByUserId(friend.getUserId()) == null || 
            repository.findUserByUserId(friend.getFriendId()) == null) 
            throw new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                 "Invalid creating relationship between Users ");


        switch (event.getEventType()) { 

            case FRIEND_ADDED: 
                log.info("Adding friend relationship ");
                repository.addFriend(
                    friend.getUserId(), 
                    friend.getFriendId(), 
                    friend.getCreatedDate(), 
                    friend.getLastModifiedDate()
                );
                return Mono.just(friend);


            case FRIEND_REMOVED: 
                log.info("Removing friend connection");
                repository.removeFriend(friend.getUserId(), friend.getFriendId());
                
                return Mono.just(friend);



            default: 
                return Mono.just(friend);

        }
    }
}
