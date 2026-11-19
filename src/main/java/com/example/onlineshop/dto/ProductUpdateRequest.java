package com.example.onlineshop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest (
        @Size(min = 2, max = 50)
        String name,

        @Size(max = 1000)
        String description,

        @DecimalMin(value = "0.0",  inclusive = false)
        BigDecimal price,

        Long categoryId
){
}
