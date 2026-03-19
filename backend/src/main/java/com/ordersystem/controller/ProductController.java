package com.ordersystem.controller;

import com.ordersystem.dto.ProductRequest;
import com.ordersystem.dto.ProductResponse;
import com.ordersystem.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse createdProduct = productService.createProduct(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdProduct);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return ResponseEntity.ok(productService.listProducts());
    }

    @GetMapping("/api/categories/{categoryId}/products")
    public ResponseEntity<List<ProductResponse>> listProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.listProductsByCategory(categoryId));
    }
}
