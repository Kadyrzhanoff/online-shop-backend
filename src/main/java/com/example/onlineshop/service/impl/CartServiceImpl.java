package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.AddToCartRequest;
import com.example.onlineshop.dto.CartItemResponse;
import com.example.onlineshop.dto.CartResponse;
import com.example.onlineshop.entity.*;
import com.example.onlineshop.exception.*;
import com.example.onlineshop.repository.CartItemRepository;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.repository.VariantStockRepository;
import com.example.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final VariantStockRepository variantStockRepository;

    @Override
    @Transactional
    public CartItemResponse addToCart(
            String username,
            AddToCartRequest request
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

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

        validateProductAvailability(stock);

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

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

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
                .filter(CartItemResponse::available)
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemResponses.stream()
                .filter(CartItemResponse::available)
                .mapToInt(CartItemResponse::quantity)
                .sum();

        return new CartResponse(
                cart.getId(),
                itemResponses,
                totalPrice,
                totalItems
        );
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(
            String username,
            Long itemId,
            int quantity
    ){

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Cart cart = cartRepository.findByUserId((user.getId()))
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        CartItem item =  cartItemRepository
                .findByIdAndCartId(itemId , cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        VariantStock stock = item.getStock();

        validateProductAvailability(stock);

        if (quantity > stock.getQuantity()) {
            throw new InsufficientStockException(stock.getQuantity());
        }

        if(quantity > stock.getQuantity()) {
            throw new InsufficientStockException(stock.getQuantity());
        }

        item.setQuantity(quantity);
        return toCartItemResponse(item);
    }

    @Override
    @Transactional
    public void deleteCartItem(String username, Long itemId){

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartItemNotFoundException(itemId)
                );

        CartItem item = cartItemRepository
                .findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

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

        boolean available =
                Boolean.TRUE.equals(product.getActive())
                        && stock.getQuantity() >= item.getQuantity();

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
                subtotal,
                available
        );
    }

    private void validateProductAvailability(VariantStock stock) {

        Product product = stock.getVariant().getProduct();

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ProductUnavailableException(product.getId());
        }
    }
}
