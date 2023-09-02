package com.example.user_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.domains.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    private final UserService userService;



    public UserController(
        UserRepository userRepository,
        UserService userService
        
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public Mono<User> createUser(@RequestBody @Valid Mono<User> user) { 
        return user;
    }



}
