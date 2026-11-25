package com.example.onlineshop.exception;

public class ProductUnavailableException extends RuntimeException {

    public ProductUnavailableException(Long productId) {
        super("Product with id " + productId + " is unavailable");
    }
}