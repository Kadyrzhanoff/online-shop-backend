package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.entity.*;
import com.example.onlineshop.exception.ProductNotFoundException;
import com.example.onlineshop.exception.VariantNotFoundException;
import com.example.onlineshop.repository.CategoryRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.ProductVariantRepository;
import com.example.onlineshop.repository.VariantStockRepository;
import com.example.onlineshop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.onlineshop.exception.CatgoryNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final VariantStockRepository variantStockRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new CatgoryNotFoundException(request.categoryId())
                );

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        for (ProductVariantRequest variantRequest : request.variants()) {

            ProductVariant variant = new ProductVariant();

            variant.setColor(variantRequest.color());
            variant.setColorImageUrl(variantRequest.colorImageUrl());
            variant.setProductImageUrl(variantRequest.productImageUrl());
            variant.setProduct(savedProduct);

            ProductVariant savedVariant = productVariantRepository.save(variant);

            Set<ClothingSize> usedSizes = new HashSet<>();

            for (VariantStockRequest stockRequest : variantRequest.stocks()) {

                if (!usedSizes.add(stockRequest.size())) {
                    throw new IllegalArgumentException(
                            "Duplicate size: " + stockRequest.size()
                        );
                }

                VariantStock stock = new VariantStock();

                stock.setSize(stockRequest.size());
                stock.setQuantity(stockRequest.quantity());
                stock.setVariant(savedVariant);

                variantStockRepository.save(stock);
            }
        }

        return toProductResponse(savedProduct);
    }

    private ProductResponse toProductResponse(Product product) {

        List<ProductVariant> variants =
                productVariantRepository.findByProductId(product.getId());

        List<ProductVariantResponse> variantResponses = new ArrayList<>();

        int totalStock = 0;

        for (ProductVariant variant : variants) {

            List<VariantStock> stocks =
                    variantStockRepository.findByVariantId(variant.getId());

            List<VariantStockResponse> stockResponses = stocks.stream()
                            .map(stock -> new VariantStockResponse(
                                    stock.getId(),
                                    stock.getSize(),
                                    stock.getQuantity()
                            )).toList();

            int variantTotal = stocks.stream().
                    mapToInt(VariantStock::getQuantity)
                    .sum();

            totalStock += variantTotal;

            variantResponses.add(
                    new ProductVariantResponse(
                            variant.getId(),
                            variant.getColor(),
                            variant.getColorImageUrl(),
                            variant.getProductImageUrl(),
                            stockResponses
                    )
            );
        }

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                totalStock,
                variantResponses
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id));

        return toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(
            Long categoryId,
            int page,
            int size,
            ClothingSize clothingSize,
            String sortBy
    ) {
        Sort sort = switch (sortBy) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            case "nameAsc" -> Sort.by("name").ascending();
            default -> Sort.by("id").descending();
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products;

        if(clothingSize != null) {
            products = productRepository.findByCategoryAndSize(
                    categoryId, clothingSize, pageable
            );
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        }  else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::toProductResponse);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(
            Long id,
            ProductUpdateRequest request
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (request.name() != null) {
            product.setName(request.name());
        }

        if (request.description() != null) {
            product.setDescription(request.description());
        }

        if (request.price() != null) {
            product.setPrice(request.price());
        }

        if (request.categoryId() != null) {

            Category category = categoryRepository
                    .findById(request.categoryId())
                    .orElseThrow(() ->
                            new CatgoryNotFoundException(request.categoryId()));

            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);

        return toProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse addVariant(
            Long productId,
            ProductVariantRequest request
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setColor(request.color());
        variant.setColorImageUrl(request.colorImageUrl());
        variant.setProductImageUrl(request.productImageUrl());

        productVariantRepository.save(variant);

        Set<ClothingSize> usedSizes = new HashSet<>();

        for (VariantStockRequest stockRequest : request.stocks()) {

            if (!usedSizes.add(stockRequest.size())) {
                throw new IllegalArgumentException(
                        "Duplicate size: " + stockRequest.size()
                );
            }

            VariantStock stock = new VariantStock();
            stock.setVariant(variant);
            stock.setSize(stockRequest.size());
            stock.setQuantity(stockRequest.quantity());

            variantStockRepository.save(stock);
        }

        return toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateVariant(
            Long variantId,
            VariantUpdateRequest request
    ) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        if (request.color() != null) {
            variant.setColor(request.color());
        }

        if (request.colorImageUrl() != null) {
            variant.setColorImageUrl(request.colorImageUrl());
        }

        if (request.productImageUrl() != null) {
            variant.setProductImageUrl(request.productImageUrl());
        }

        Product product = variant.getProduct();

        return toProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteVariant(Long variantId) {

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        variantStockRepository.deleteByVariantId(variantId);

        productVariantRepository.delete(variant);
    }

    @Transactional
    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        List<ProductVariant> variants =
                productVariantRepository.findByProductId(productId);

        for (ProductVariant variant : variants) {
            variantStockRepository.deleteByVariantId(variant.getId());
            productVariantRepository.delete(variant);
        }

        productRepository.delete(product);
    }
}
