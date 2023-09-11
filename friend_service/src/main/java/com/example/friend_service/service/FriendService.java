

package com.example.friend_service.service;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.domains.events.DomainEvent;
import com.example.friend_service.domains.events.EventType;
import com.example.friend_service.external.UserClient;
import com.example.friend_service.repository.FriendRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class FriendService {
    
    private static final Long serialVersionId = 1L;

    private final UserClient client;

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository, UserClient client) { 
        this.friendRepository = friendRepository;
        this.client = client;
    }

    /**
     * Create a new Friend Entity with a supplied callback that allows you submit a domain event
     * to another shared service before finalising the transaction
     * 
     * @param friend
     * @param callback
     * @return
     */
    public Mono<Friend> save(Friend friend, Consumer<Friend> callback) { 

        if (friend.getUserId() == friend.getFriendId())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Unabled to befriend yourself");
        
        if (friend.getUserId() == null || friend.getFriendId() == null) 
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Error in creating a new friend entity");
        
        // Check if the friendId exist in the User Service 
        if (client.getUser(friend.getFriendId()).single() != null) 
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Invalid Friend Id ");

        // Save to the database
        return friendRepository
            .save(friend)
            .map(Friend::getId)
            .flatMap(id -> friendRepository
                .findById(id)
                .single()
                .delayUntil(item -> Mono.fromRunnable(() -> callback.accept(item))));
    } 

    /**
     * Retrieves all Friend Entities under `userId`
     * 
     * @param userId
     * @return emits {@link Flux<User>} the result of the transaction
     */
    public Flux<Friend> getAllByUserId(final Long userId) {
        return friendRepository.findByUserId(userId);
    }

    /**
     * Retrieves the Friend Object
     * 
     * @param id of Friend entity
     * @return emits {@link Mono<User>} the result of the transaction
     */
    public Mono<Friend> getOne(final String id) {
        return friendRepository.findById(id);
    }

    /**
     * Deletes existing Friend entity
     * 
     * @param friend
     * @param callback {@link Consumer<Friend>} allows you to ensure the transaction is successful else, a rollback is executed
     */
    public Mono<Friend> deleteOne(Friend friend, Consumer<Friend> callback) {
        return friendRepository
            .getFriend(friend.getUserId(), friend.getUserId())
            .flatMap(object -> { 
                friend.setId(object.getId());

                return friendRepository.delete(friend).then(Mono.just(friend));
            })
            .delayUntil(item -> Mono.fromRunnable(() -> callback.accept(item))).single();
    }

    /**
     * Updates an existing Friend entity by its unique ID
     * 
     * 
     * @param friend {@link Friend} entity contains the updated values
     * @param callback {@link Consumer<Friend>} allows you to ensure the transaction is successful else, a rollback is executed
     * @return emits {@link Mono<User>} the result of the transaction
     */
    public Mono<Friend> partialUpdate(Friend friend, Consumer<Friend> callback) { 
        Assert.notNull(friend.getId(), "Friend Id cannot be null");

        return friendRepository.findById(friend.getId())
            .flatMap(current -> { 

                if (friend.getUserId() != null) current.setUserId(friend.getUserId());
                if (friend.getFriendId() != null) current.setFriendId(friend.getFriendId());

                return friendRepository.save(current);
            })
            .delayUntil(item -> Mono.fromRunnable(() -> callback.accept(item)))
            .single();
    }

    /**
     * Retrieves the Friend entity using `userId` and `friendId`
     * 
     * @param userId
     * @param friendId
     * @return
     */
    public Mono<Friend> getFriend(final Long userId, final Long friendId) { 
        return friendRepository.getFriend(userId, friendId).single();
    }
}
