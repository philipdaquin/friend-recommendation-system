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


            case FRIEND_REMOVED: 
                log.info("Removing friend connection");
                repository.removeFriend(friend.getUserId(), friend.getFriendId());

            default: 
                break;
        }
    }
}
