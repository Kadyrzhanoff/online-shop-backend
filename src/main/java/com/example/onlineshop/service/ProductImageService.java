package com.example.onlineshop.service;

import com.example.onlineshop.dto.ProductImageRequest;
import com.example.onlineshop.dto.ProductImageResponse;
import com.example.onlineshop.dto.ProductVariantResponse;

import java.util.List;

public interface ProductImageService {

    List<ProductImageResponse> getImages(Long variantId);

    ProductImageResponse addImage(
            Long variantId,
            ProductImageRequest request
    );

    ProductImageResponse updateImage(
            Long variantId,
            Long imageId,
            ProductImageRequest request
    );

    void deleteImage(Long variantId, Long imageId);

    List<ProductImageResponse> reorderImages(
            Long variantId,
            List<Long> imageIds
    );

    void setMainImage(Long variantId, String imageUrl);
}
