package com.example.onlineshop.repository;

import com.example.onlineshop.entity.ProductVariant;
import com.example.onlineshop.entity.VariantStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantStockRepository
    extends JpaRepository<VariantStock, Long> {

    List<VariantStock> findByVariantId(Long variantId);

    void deleteByVariantId(Long variantId);
}
