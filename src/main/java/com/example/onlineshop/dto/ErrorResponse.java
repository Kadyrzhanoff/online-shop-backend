package com.example.onlineshop.dto;

public record ErrorResponse (
        int status,
        String message,
        String path
){
}
