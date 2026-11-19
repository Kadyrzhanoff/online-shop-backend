package com.example.onlineshop.controller;


import com.example.onlineshop.dto.ProductResponse;
import com.example.onlineshop.dto.VariantUpdateRequest;
import com.example.onlineshop.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/variants")
@RequiredArgsConstructor
public class VariantController {

    private final ProductServiceImpl productService;

    @PatchMapping("/{variantId}")
    public ResponseEntity<ProductResponse> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody VariantUpdateRequest request
            ) {
        ProductResponse response =
                productService.updateVariant(variantId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deleteVariant(
            @PathVariable Long variantId
    ) {
        productService.deleteVariant(variantId);

        return ResponseEntity.noContent().build();
    }
}
