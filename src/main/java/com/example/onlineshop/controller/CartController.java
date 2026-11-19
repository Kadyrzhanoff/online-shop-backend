package com.example.onlineshop.controller;


import com.example.onlineshop.dto.AddToCartRequest;
import com.example.onlineshop.dto.CartItemResponse;
import com.example.onlineshop.dto.CartResponse;
import com.example.onlineshop.dto.UpdateCartItemRequest;
import com.example.onlineshop.service.impl.CartServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request
    ) {
        
        String username = authentication.getName();

        CartItemResponse response = cartService.addToCart(username, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            Authentication authentication
    ) {
        String username = authentication.getName();

        CartResponse response = cartService.getCart(username);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            Authentication authentication,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        CartItemResponse response = cartService.updateCartItem(
                authentication.getName(),
                itemId,
                request.quantity()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> deleteCartItem(
            Authentication authentication,
            @PathVariable Long itemId
    ) {

        cartService.deleteCartItem(authentication.getName(), itemId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    private ResponseEntity<Void> clearCart(
            Authentication authentication
    ) {

        cartService.clearCart(authentication.getName());

        return ResponseEntity.noContent().build();
    }
}
