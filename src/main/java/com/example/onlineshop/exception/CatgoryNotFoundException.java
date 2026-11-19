package com.example.onlineshop.exception;

public class CatgoryNotFoundException extends RuntimeException {

    public CatgoryNotFoundException(Long id) {
        super("Category with id " + id + " not found");
    }
}