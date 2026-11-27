package com.example.onlineshop.dto;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record VariantUpdateRequest (

        @Size(min = 1, max = 100)
        String color,

        String colorImageUrl,

        @Size(min = 1, max = 2048)
        @URL(protocol = "https")
        String productImageUrl
) {
}
