package com.example.onlineshop.dto;

public record ProductImageResponse (
        Long id,
        String imageUrl,
        Integer sortOrder
) {
}
