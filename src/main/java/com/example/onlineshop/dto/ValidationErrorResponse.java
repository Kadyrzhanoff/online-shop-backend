package com.example.onlineshop.dto;

import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String message,
        String path,
        Map<String,String> errors
) {}