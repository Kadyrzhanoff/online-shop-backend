package com.example.onlineshop.repository;

import com.example.onlineshop.entity.Favorite;
import com.example.onlineshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserIdAndProductActiveTrue(Long userId);

    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductIdAndProductActiveTrue(Long userId, Long productId);
}
