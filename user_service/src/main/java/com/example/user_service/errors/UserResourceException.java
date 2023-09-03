package com.example.user_service.errors;

public class UserResourceException extends RuntimeException {
    public UserResourceException(String message) { 
        super(message);
    }
}
