package com.example.onlineshop.dto;

import jakarta.validation.constraints.Size;

public record VariantUpdateRequest (
        @Size(min = 1, max = 100)
        String color,

        String colorImageUrl,

        String productImageUrl
) {
}
