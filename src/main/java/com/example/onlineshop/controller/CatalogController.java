package com.example.onlineshop.controller;


import com.example.onlineshop.dto.ProductResponse;
import com.example.onlineshop.entity.ClothingSize;
import com.example.onlineshop.entity.ProductSort;
import com.example.onlineshop.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false)
            Long categoryId,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "10")
            @Min(0) @Max(100)
            int size,

            @RequestParam(required = false)
            ClothingSize clothingSize,

            @RequestParam(defaultValue = "newest")
            ProductSort sortBy
    ) {

        Page<ProductResponse> products =
                productService.getProducts(categoryId, page, size, clothingSize, sortBy.name());

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id
    ) {
        ProductResponse product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }
}
