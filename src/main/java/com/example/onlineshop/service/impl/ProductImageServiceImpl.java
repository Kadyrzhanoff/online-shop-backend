package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.ProductImageRequest;
import com.example.onlineshop.dto.ProductImageResponse;
import com.example.onlineshop.entity.ProductImage;
import com.example.onlineshop.entity.ProductVariant;
import com.example.onlineshop.exception.InvalidImageOrderException;
import com.example.onlineshop.exception.ProductImageLimitException;
import com.example.onlineshop.exception.ProductImageNotFoundException;
import com.example.onlineshop.exception.VariantNotFoundException;
import com.example.onlineshop.repository.ProductImageRepository;
import com.example.onlineshop.repository.ProductVariantRepository;
import com.example.onlineshop.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private static final int MAX_IMAGES = 5;

    private final ProductImageRepository productImageRepository;
    private final ProductVariantRepository productVariantRepository;

    private ProductImageResponse toResponse(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getSortOrder()
        );
    }

    private ProductVariant getVariantForUpdate(Long variantId) {
        return productVariantRepository
                .findByIdForUpdate(variantId)
                .orElseThrow(() ->
                        new VariantNotFoundException(variantId));
    }

    private ProductImage getImage(Long variantId, Long imageId) {
        return productImageRepository
                .findByIdAndVariantId(imageId, variantId)
                .orElseThrow(() ->
                        new ProductImageNotFoundException(imageId));
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImages(Long variantId) {

        if (!productVariantRepository.existsById(variantId)) {
            throw new VariantNotFoundException(variantId);
        }

        return productImageRepository
                .findByVariantIdOrderBySortOrderAsc(variantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductImageResponse addImage(Long variantId, ProductImageRequest request) {

        ProductVariant variant = getVariantForUpdate(variantId);

        List<ProductImage> images = productImageRepository
                .findByVariantIdOrderBySortOrderAsc(variantId);

        if (images.size() >= MAX_IMAGES) {
            throw new ProductImageLimitException(variantId);
        }

        int sortOrder = 0;

        while (sortOrder < MAX_IMAGES) {
            final int position = sortOrder;

            boolean occupied = images.stream()
                    .anyMatch(image -> image.getSortOrder() == position);

            if (!occupied) {
                break;
            }
            sortOrder++;
        }

        ProductImage image = new ProductImage();

        image.setVariant(variant);
        image.setImageUrl(request.imageUrl());
        image.setSortOrder(sortOrder);

        ProductImage savedImage = productImageRepository.save(image);

        return toResponse(savedImage);
    }

    @Override
    @Transactional
    public ProductImageResponse updateImage(
            Long variantId,
            Long imageId,
            ProductImageRequest request
    ) {
        getVariantForUpdate(variantId);

        ProductImage image = getImage(variantId, imageId);

        image.setImageUrl(request.imageUrl());

        return toResponse(image);
    }

    @Override
    @Transactional
    public void deleteImage(Long variantId, Long imageId) {

        getVariantForUpdate(variantId);

        ProductImage image = getImage(variantId, imageId);

        productImageRepository.delete(image);
        productImageRepository.flush();

        List<ProductImage> images = productImageRepository
                .findByVariantIdOrderBySortOrderAsc(variantId);

        for (int i = 0; i < images.size(); i++) {

            ProductImage currentImage = images.get(i);

            if (currentImage.getSortOrder() != i) {
                currentImage.setSortOrder(i);
                productImageRepository.flush();
            }
        }
    }

    @Override
    @Transactional
    public List<ProductImageResponse> reorderImages(Long variantId, List<Long> imageIds) {

        getVariantForUpdate(variantId);

        List<ProductImage> images = productImageRepository
                .findByVariantIdOrderBySortOrderAsc(variantId);

        Map<Long, ProductImage> imagesById = images.stream()
                .collect(Collectors.toMap(
                        ProductImage::getId,
                        Function.identity()
                ));

        if (imageIds == null
                || imageIds.size() != images.size()
                || !new HashSet<>(imageIds).equals(imagesById.keySet())) {

            throw new InvalidImageOrderException();
        }

        for (int i = 0; i < images.size(); i++) {

            ProductImage image = imagesById.get(imageIds.get(i));

            image.setSortOrder(i);
        }

        return imageIds.stream()
                .map(imagesById::get)
                .map(this::toResponse)
                .toList();

    }

    @Override
    @Transactional
    public void setMainImage(Long variantId, String imageUrl) {

        getVariantForUpdate(variantId);

        List<ProductImage> images = productImageRepository
                .findByVariantIdOrderBySortOrderAsc(variantId);

        ProductImageRequest request =
                new ProductImageRequest(imageUrl);

        if (images.isEmpty()){
            addImage(variantId, request);
        } else {
            images.getFirst().setImageUrl(imageUrl);
        }
    }
}