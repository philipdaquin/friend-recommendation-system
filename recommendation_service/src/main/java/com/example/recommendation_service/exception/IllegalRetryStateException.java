package com.example.recommendation_service.exception;

public class IllegalRetryStateException extends RuntimeException {
    public IllegalRetryStateException(String message) { 
        super(message);
    }
}
