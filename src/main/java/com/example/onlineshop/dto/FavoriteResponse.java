package com.example.onlineshop.dto;

import java.math.BigDecimal;

public record FavoriteResponse (
        Long id,
        Long productId,
        String name,
        BigDecimal price,
        String imageUrl
) {
}
