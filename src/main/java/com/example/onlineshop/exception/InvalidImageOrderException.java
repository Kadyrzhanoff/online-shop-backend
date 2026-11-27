package com.example.onlineshop.exception;

public class InvalidImageOrderException extends RuntimeException {
    public InvalidImageOrderException() {

        super("Invalid image order");
    }
}
