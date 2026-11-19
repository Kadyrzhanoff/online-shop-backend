package com.example.onlineshop.service;

import com.example.onlineshop.dto.StockResponse;
import com.example.onlineshop.entity.VariantStock;
import com.example.onlineshop.exception.StockNotFoundException;
import com.example.onlineshop.repository.VariantStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final VariantStockRepository variantStockRepository;

    @Transactional
    public StockResponse setStock(Long id, int quantity) {

        VariantStock stock = variantStockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException(id));

        stock.setQuantity(quantity);

        return new StockResponse(
                stock.getId(),
                stock.getSize(),
                stock.getQuantity()
        );
    }

    @Transactional
    public StockResponse addStock(Long id, int quantity) {

        VariantStock stock = variantStockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException(id));

        int  newQuantity = Math.addExact(stock.getQuantity(), quantity);
        stock.setQuantity(newQuantity);

        return new StockResponse(
                stock.getId(),
                stock.getSize(),
                stock.getQuantity()
        );
    }
}
