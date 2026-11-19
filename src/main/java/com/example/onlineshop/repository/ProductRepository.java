package com.example.onlineshop.repository;

import com.example.onlineshop.entity.ClothingSize;
import com.example.onlineshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(
            Long categoryId,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT p
    FROM Product p
    JOIN ProductVariant pv ON pv.product = p
    JOIN VariantStock vs ON vs.variant = pv
    WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
      AND vs.size = :size
      AND vs.quantity > 0
    """)
    Page<Product> findByCategoryAndSize(
            @Param("categoryId") Long categoryId,
            @Param("size") ClothingSize size,
            Pageable pageable
    );
}
