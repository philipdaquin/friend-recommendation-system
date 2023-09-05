package com.example.friend_service;

import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.from(FriendServiceApplication::main)
        .with(TestFriendServiceApplication.class)
        .run(args);
    }
}
