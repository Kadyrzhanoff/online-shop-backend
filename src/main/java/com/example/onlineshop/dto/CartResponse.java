package com.example.onlineshop.dto;

import com.example.onlineshop.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse (
        Long cartId,
        List<CartItemResponse> items,
        BigDecimal totalPrice,
        Integer totalItems
){ }