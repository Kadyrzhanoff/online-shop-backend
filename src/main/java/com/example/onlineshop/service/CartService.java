package com.example.onlineshop.service;

import com.example.onlineshop.dto.AddToCartRequest;
import com.example.onlineshop.dto.CartItemResponse;
import com.example.onlineshop.dto.CartResponse;
import com.example.onlineshop.entity.Cart;
import com.example.onlineshop.entity.CartItem;

public interface CartService {

    CartItemResponse addToCart(String username, AddToCartRequest request);

    CartResponse getCart(String username);

    CartItemResponse updateCartItem(
            String username,
            Long itemId,
            int quantity
    );

    void deleteCartItem(String username, Long itemId);

    void clearCart(String username);
}
