package com.example.onlineshop.exception;

public class VariantNotFoundException extends RuntimeException {

    public VariantNotFoundException(Long id) {
        super("Variant not found with id: " + id);
    }
}
