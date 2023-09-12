package com.example.friend_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.example.friend_service.domains.Friend;
import com.example.friend_service.repository.FriendRepository;
import com.example.friend_service.service.FriendProducerService;
import com.example.friend_service.service.FriendService;
import com.example.friend_service.service.KafkaProducerService;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/v1")
public class FriendController {
    
    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    private final FriendService friendService;

    private final FriendProducerService friendProducer;

    private final FriendRepository friendRepository;

    public FriendController(
        FriendService friendService,
        FriendRepository friendRepository,
        FriendProducerService friendProducer
    ) {
        this.friendService = friendService;
        this.friendRepository = friendRepository;
        this.friendProducer = friendProducer;
    }

    /**
     * Add a user-friend connection to the user. This performs a dual write that expects both external services to 
     * cooperate with each other to perform a single transaction. If an error arises, the transaction is immediately rolledback.
     * 
     * @param id
     * @param friendId
     * @return
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/users/{id}/command/add-friend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Friend> addFriend(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "FriendId is empty") @RequestParam(required = true, value = "friendId") final Long friendId
    ) {
        // Check if the UserId is already friends with FriendsId
        if (friendRepository.getFriend(id, friendId).single() == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "The userId and friendid already exist!");
        }

        Friend friend = new Friend(id, friendId);

        // Execute dual writes to both local database and messaging broker
        return friendProducer.addFriendCallback(friend);
    }

    /**
     * Removes a friend. This performs a dual write that expects both external services to 
     * cooperate with each other to perform a single transaction. If an error arises, the transaction is immediately rolledback.
     * 
     * @param id
     * @param friendId
     * @return
     */
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/users/{id}/command/remove-friend")
    public Mono<Friend> removeFriend(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "FriendId empty be empty") @RequestParam(required = true, value = "friendId") final Long friendId
    ) { 
        // Check if the UserId is already friends with FriendsId
        if (friendRepository.getFriend(id, friendId).single() == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "The userId and friendid does not exist");
        }
        Friend friend = new Friend(id, friendId);

        // Execute dual writes to both local database and messaging broker
        return friendProducer.removeFriendCallback(friend);    
    }

    /**
     * Retrieves all the friends from the User by the `userId`
     * 
     * @param userId
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/users/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Friend> getUserFriends(@PathVariable final Long userId) {
        return friendService.getAllByUserId(userId);

    }

    /**
     * Retrieves a Friend Entity by its id
     * 
     * @param friendId
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/friends/{friendId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Friend> getEntity(@PathVariable final String friendId) {
        return friendService.getOne(friendId);
    }
}
