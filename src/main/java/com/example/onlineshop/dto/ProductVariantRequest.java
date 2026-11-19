package com.example.onlineshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProductVariantRequest (

        @NotBlank
        String color,

        String colorImageUrl,

        String productImageUrl,

        @NotEmpty
        List<@Valid VariantStockRequest> stocks
) { }
