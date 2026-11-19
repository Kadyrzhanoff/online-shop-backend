package com.example.onlineshop.dto;

import com.example.onlineshop.entity.ClothingSize;

public record VariantStockResponse(
        Long id,
        ClothingSize size,
        Integer quantity
){ }