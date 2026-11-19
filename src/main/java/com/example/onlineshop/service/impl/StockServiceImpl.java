package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.StockResponse;
import com.example.onlineshop.entity.VariantStock;
import com.example.onlineshop.exception.StockNotFoundException;
import com.example.onlineshop.repository.VariantStockRepository;
import com.example.onlineshop.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final VariantStockRepository variantStockRepository;

    @Override
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

    @Override
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