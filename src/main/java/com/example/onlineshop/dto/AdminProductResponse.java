package com.example.onlineshop.dto;

import java.math.BigDecimal;
import java.util.List;

public record AdminProductResponse (
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        String categoryName,
        Boolean active,
        Integer totalStock,
        List<ProductVariantResponse> variants
) {
}
