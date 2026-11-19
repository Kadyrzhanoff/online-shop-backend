package com.example.onlineshop.exception;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(Long id) {
        super("Could not find stock with id " + id);
    }
}
