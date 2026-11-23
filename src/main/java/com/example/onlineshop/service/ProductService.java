package com.example.onlineshop.service;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.entity.ClothingSize;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getProducts(
            Long categoryId,
            int page,
            int size,
            ClothingSize clothingSize,
            String sortBy
    );

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    ProductResponse addVariant(Long productId, ProductVariantRequest request);

    ProductResponse updateVariant(Long variantId, VariantUpdateRequest request);

    void deleteVariant(Long variantId);

    void deleteProduct(Long productId);
}
