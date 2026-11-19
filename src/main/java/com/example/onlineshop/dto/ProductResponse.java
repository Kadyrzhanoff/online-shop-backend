package com.example.onlineshop.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse (

        Long id,
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        String categoryName,
        Integer totalStock,
        List<ProductVariantResponse> variants
) { }