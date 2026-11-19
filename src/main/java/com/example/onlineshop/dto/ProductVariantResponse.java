package com.example.onlineshop.dto;

import java.util.List;

public record ProductVariantResponse (
        Long id,
        String color,
        String colorImageUrl,
        String productImageUrl,
        List<VariantStockResponse> stocks
) { }