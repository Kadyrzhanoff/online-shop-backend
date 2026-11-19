package com.example.onlineshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest (

        @NotNull
        Long stockId,

        @NotNull
        @Min(1)
        Integer quantity
) { }