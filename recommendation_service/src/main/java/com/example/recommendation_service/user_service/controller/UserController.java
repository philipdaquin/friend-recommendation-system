package com.example.recommendation_service.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.recommendation_service.repository.Neo4JUserRepository;
import com.example.recommendation_service.user_service.domains.User;
import com.example.recommendation_service.user_service.ranked.RankedUser;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final Neo4JUserRepository repository;

    public UserController(Neo4JUserRepository repository) { 
        this.repository = repository;
    }
    
    /**
     * Get all mutual friends
     * 
     * @param id
     * @param friendId
     * @return
     */
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/users/{id}/command/find-mutual-friends", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<User> findMutuals(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id, 
        @NotNull(message = "FriendId empty be empty") @RequestParam(required = true, value = "friendId") final Long friendId
    ) {
        return repository.findMutualFriends(id, friendId);
    }

    /**
     * Get all recommended users
     * 
     * @param id
     * @return
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @GetMapping(path = "/users/{id}/command/recommend-friends", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<RankedUser> recommendFriends(
        @NotNull(message = "UserId cannot be null") @PathVariable final Long id
    ) {
        return repository.recommendFriends(id, RankedUser.class);
    }
}
