package com.example.onlineshop.controller;


import com.example.onlineshop.dto.*;
import com.example.onlineshop.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest  request
            ) {

        ProductResponse response = productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminProductResponse> getProductById(
            @PathVariable Long id
    ){
        return ResponseEntity
                .ok(productService.getAdminProductById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest  request
    ) {
        ProductResponse response =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ProductResponse> addVariant(
            @PathVariable Long productId,
            @Valid @RequestBody ProductVariantRequest request
            ) {
        ProductResponse response = productService.addVariant(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<Page<AdminProductResponse>> getAdminProducts(
            @RequestParam(required = false)
            Boolean active,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100)
            int size
    ) {
        Page<AdminProductResponse> response = productService.getAdminProducts(active, page, size);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}/restore")
    public ResponseEntity<AdminProductResponse> restoreProduct(
            @PathVariable Long id
    ){
        AdminProductResponse response = productService.restoreProduct(id);

        return ResponseEntity.ok(response);
    }
}