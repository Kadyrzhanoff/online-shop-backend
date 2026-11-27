package com.example.onlineshop.exception;

public class ProductImageNotFoundException extends RuntimeException {
    public ProductImageNotFoundException(Long imageId) {
        super("Product image with id " + imageId + " not found");
    }
}
