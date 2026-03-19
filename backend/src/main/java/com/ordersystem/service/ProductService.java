package com.ordersystem.service;

import com.ordersystem.dto.ProductRequest;
import com.ordersystem.dto.ProductResponse;
import com.ordersystem.entity.Category;
import com.ordersystem.entity.Product;
import com.ordersystem.repository.CategoryRepository;
import com.ordersystem.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product savedProduct = productRepository.save(toEntity(request));
        return toResponse(savedProduct);
    }

    public List<ProductResponse> listProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> listProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse updateStock(Long productId, Integer stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + productId));
        product.setStock(stock);
        return toResponse(productRepository.save(product));
    }

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }

        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getActive(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }
}
