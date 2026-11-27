package com.example.onlineshop.controller;


import com.example.onlineshop.dto.ProductImageOrderRequest;
import com.example.onlineshop.dto.ProductImageRequest;
import com.example.onlineshop.dto.ProductImageResponse;
import com.example.onlineshop.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/variants/{variantId}/images")
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping
    public List<ProductImageResponse> getImages(
            @PathVariable Long variantId
    ) {
        return productImageService.getImages(variantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageResponse addImage(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductImageRequest request
    ) {
        return productImageService.addImage(variantId, request);
    }

    @PatchMapping("/{imageId}")
    public ProductImageResponse updateImage(
            @PathVariable Long variantId,
            @PathVariable Long imageId,
            @Valid @RequestBody ProductImageRequest request
    ) {
        return productImageService.updateImage(variantId, imageId, request);
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @PathVariable Long variantId,
            @PathVariable Long imageId
    ) {
        productImageService.deleteImage(variantId, imageId);
    }

    @PutMapping("/order")
    public List<ProductImageResponse> reorderImages(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductImageOrderRequest request
    ) {
        return productImageService.reorderImages(variantId, request.imageIds());
    }

}
