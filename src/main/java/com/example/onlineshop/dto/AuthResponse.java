package com.example.onlineshop.dto;

public record AuthResponse (
        String accessToken,
        String tokenType
) {}
