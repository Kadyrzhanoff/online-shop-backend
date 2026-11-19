package com.example.onlineshop.service;

import com.example.onlineshop.dto.AddToCartRequest;
import com.example.onlineshop.dto.CartItemResponse;
import com.example.onlineshop.dto.CartResponse;
import com.example.onlineshop.entity.*;
import com.example.onlineshop.exception.CartItemNotFoundException;
import com.example.onlineshop.exception.InsufficientStockException;
import com.example.onlineshop.exception.StockNotFoundException;
import com.example.onlineshop.exception.UserAlreadyExistsException;
import com.example.onlineshop.repository.CartItemRepository;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.repository.VariantStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final VariantStockRepository variantStockRepository;

    @Transactional
    public CartItemResponse addToCart(
            String username,
            AddToCartRequest request
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() ->{
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        VariantStock stock = variantStockRepository
                .findById(request.stockId())
                .orElseThrow(() ->
                        new StockNotFoundException(request.stockId()));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndStockId(
                        cart.getId(),
                        stock.getId()
                )
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setStock(stock);
                    newItem.setQuantity(0);
                    return newItem;
                });

        int newQuantity = Math.addExact(
                cartItem.getQuantity(),
                request.quantity()
        );

        if(newQuantity > stock.getQuantity()) {
            throw new InsufficientStockException(stock.getQuantity());
        }

        cartItem.setQuantity(newQuantity);

        CartItem savedItem = cartItemRepository.save(cartItem);

        return toCartItemResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserAlreadyExistsException("Username not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElse(null);

        if (cart == null) {
            return new CartResponse(
                    null,
                    List.of(),
                    BigDecimal.ZERO,
                    0
            );
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal totalPrice = itemResponses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();

        return new CartResponse(
                cart.getId(),
                itemResponses,
                totalPrice,
                totalItems
        );
    }

    @Transactional
    public CartItemResponse updateCartItem(
            String username,
            Long itemId,
            int quantity
    ){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username not found"));

        Cart cart = cartRepository.findByUserId((user.getId()))
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        CartItem item =  cartItemRepository
                .findByIdAndCartId(itemId , cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        VariantStock stock = item.getStock();

        if(quantity > stock.getQuantity()) {
            throw new InsufficientStockException(stock.getQuantity());
        }

        item.setQuantity(quantity);
        return toCartItemResponse(item);
    }

    @Transactional
    public void deleteCartItem(String username, Long itemId){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartItemNotFoundException(itemId)
                );

        CartItem item = cartItemRepository
                .findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("Username not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElse(null);

        if (cart == null) {
            return;
        }

        cartItemRepository.deleteByCartId(cart.getId());
    }

    private CartItemResponse toCartItemResponse(CartItem item) {

        VariantStock stock = item.getStock();
        ProductVariant variant = stock.getVariant();
        Product product = variant.getProduct();

        BigDecimal subtotal = product.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getId(),
                stock.getId(),
                product.getName(),
                variant.getColor(),
                stock.getSize().name(),
                product.getPrice(),
                item.getQuantity(),
                subtotal
        );
    }
}
