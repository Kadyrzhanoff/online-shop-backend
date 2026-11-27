package com.example.onlineshop.repository;

import com.example.onlineshop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByVariantIdOrderBySortOrderAsc(Long variantId);

    long countByVariantId(Long variantId);

    Optional<ProductImage> findByIdAndVariantId(Long id, Long variantId);
}
