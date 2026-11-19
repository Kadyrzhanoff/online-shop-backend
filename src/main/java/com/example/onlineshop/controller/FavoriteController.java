package com.example.onlineshop.controller;

import com.example.onlineshop.dto.FavoriteResponse;
import com.example.onlineshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{productId}")
    public ResponseEntity<FavoriteResponse> addFavorite(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        FavoriteResponse response = favoriteService.addFavorite(
                authentication.getName(),
                productId
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            Authentication authentication
    ) {
        return ResponseEntity
                .ok(favoriteService.getFavorites(authentication.getName()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteFavorite(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        favoriteService.deleteFavorite(authentication.getName(), productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}/check")
    public ResponseEntity<Boolean> checkFavorite(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                favoriteService.isFavorite(
                        authentication.getName(),
                        productId)
        );
    }
}
