package com.example.user_service.errors;

public class UserResourceException extends RuntimeException {
    
    private static final Long serialVersionID = 1L;

    public UserResourceException(String message) { 
        super(message);
    }
}
