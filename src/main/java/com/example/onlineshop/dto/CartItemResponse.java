package com.example.onlineshop.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long stockId,
        String productName,
        String color,
        String size,
        BigDecimal price,
        Integer quantity,
        BigDecimal subtotal,
        boolean available
) { }