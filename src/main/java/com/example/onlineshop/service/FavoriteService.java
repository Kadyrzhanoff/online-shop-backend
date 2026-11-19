package com.example.onlineshop.service;

import com.example.onlineshop.dto.FavoriteResponse;
import com.example.onlineshop.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    FavoriteResponse addFavorite(
            String username,
            Long productId
    );

    List<FavoriteResponse> getFavorites(String username);

    void deleteFavorite(String username, Long productId);

    boolean isFavorite(String username, Long productId);
}
