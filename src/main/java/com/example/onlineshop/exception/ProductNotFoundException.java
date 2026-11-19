package com.example.onlineshop.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Poduct not found with id " + id);
    }
}
