package com.example.onlineshop.service;

import com.example.onlineshop.dto.StockResponse;

public interface StockService {

    StockResponse setStock(Long id, int quantity);

    StockResponse addStock(Long id, int quantity);


}
