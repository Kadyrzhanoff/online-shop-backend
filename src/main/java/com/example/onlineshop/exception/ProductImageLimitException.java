package com.example.onlineshop.exception;

public class ProductImageLimitException extends RuntimeException {
    public ProductImageLimitException(Long variantId) {

        super("Variant " + variantId + " cannot have more than 5 images");
    }
}
