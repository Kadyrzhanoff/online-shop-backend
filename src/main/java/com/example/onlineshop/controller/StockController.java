package com.example.onlineshop.controller;


import com.example.onlineshop.dto.StockResponse;
import com.example.onlineshop.dto.StockUpdateRequest;
import com.example.onlineshop.service.impl.StockServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockServiceImpl stockService;

    @PatchMapping("/{id}/set")
    public ResponseEntity<StockResponse> setStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request
    ) {
        return ResponseEntity.ok(
                stockService.setStock(id, request.quantity())
        );
    }

    @PatchMapping("/{id}/add")
    public ResponseEntity<StockResponse> addStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request
    ) {
        return ResponseEntity.ok(
                stockService.addStock(id, request.quantity())
        );
    }
}
