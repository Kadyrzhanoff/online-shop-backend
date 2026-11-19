package com.example.onlineshop.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(int available) {
        super("Not enough stock. Available: " + available);
    }
}
