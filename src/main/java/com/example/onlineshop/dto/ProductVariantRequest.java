package com.example.onlineshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ProductVariantRequest (

        @NotBlank
        String color,

        String colorImageUrl,

        @Size(min = 1, max = 2048)
        @URL(protocol = "https")
        String productImageUrl,

        @NotEmpty
        List<@Valid VariantStockRequest> stocks
) { }
