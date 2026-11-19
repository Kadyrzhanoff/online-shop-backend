package com.example.onlineshop.service.impl;


import com.example.onlineshop.dto.FavoriteResponse;
import com.example.onlineshop.entity.Favorite;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.entity.ProductVariant;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.exception.ProductNotFoundException;
import com.example.onlineshop.exception.UserNotFoundException;
import com.example.onlineshop.repository.FavoriteRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.ProductVariantRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public FavoriteResponse addFavorite(
            String username,
            Long productId
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(productId));

        Favorite favorite = favoriteRepository
                .findByUserIdAndProductId(user.getId(), productId)
                .orElseGet(() -> {
                    Favorite newFavorite = new Favorite();
                    newFavorite.setUser(user);
                    newFavorite.setProduct(product);

                    return favoriteRepository.save(newFavorite);
                });

        return toFavoriteResponse(favorite);
    }

    @Override
    @Transactional
    public List<FavoriteResponse> getFavorites(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());

        return favorites.stream()
                .map(this::toFavoriteResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteFavorite(String username, Long productId) {

        User user = userRepository.findByUsername(username)
                        .orElseThrow(UserNotFoundException::new);

        favoriteRepository
                .findByUserIdAndProductId(user.getId(), productId)
                .ifPresent(favoriteRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(String username, Long productId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return favoriteRepository.existsByUserIdAndProductId(user.getId(), productId);
    }

    private FavoriteResponse toFavoriteResponse(Favorite favorite) {

        Product product = favorite.getProduct();

        List<ProductVariant> variants =
                productVariantRepository.findByProductId(product.getId());

        String imageUrl = variants.isEmpty()
                ? null
                : variants.get(0).getProductImageUrl();

        return new FavoriteResponse(
                favorite.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                imageUrl
        );
    }
}
