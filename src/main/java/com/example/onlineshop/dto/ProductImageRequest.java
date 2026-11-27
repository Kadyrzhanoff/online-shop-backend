package com.example.onlineshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ProductImageRequest (

        @NotBlank
        @Size(max = 2048)
        @URL(protocol = "https")
        String imageUrl
){ }