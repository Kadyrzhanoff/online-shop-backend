package com.example.onlineshop.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Authenticated user no longer exists");
    }
}
