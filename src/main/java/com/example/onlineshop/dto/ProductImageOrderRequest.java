package com.example.onlineshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductImageOrderRequest (

        @NotNull
        @Size(max = 5)
        List<@NotNull @Positive Long> imageIds
) {
}
