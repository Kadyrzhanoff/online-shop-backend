package com.example.onlineshop.dto;

import com.example.onlineshop.entity.ClothingSize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VariantStockRequest (

        @NotNull
        ClothingSize size,

        @NotNull
        @Min(0)
        Integer quantity
) { }
