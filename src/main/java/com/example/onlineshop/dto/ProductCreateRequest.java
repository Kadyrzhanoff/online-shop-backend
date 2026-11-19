package com.example.onlineshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record ProductCreateRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal price,

        @NotNull
        Long categoryId,

        @NotEmpty
        List<@Valid ProductVariantRequest> variants
) {}