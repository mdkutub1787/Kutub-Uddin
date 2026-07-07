package com.kutub.ecommerce.ecommerce_api.controller;

import com.kutub.ecommerce.ecommerce_api.dto.ApiResponse;
import com.kutub.ecommerce.ecommerce_api.dto.ProductDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import com.kutub.ecommerce.ecommerce_api.mapper.ProductMapper;
import com.kutub.ecommerce.ecommerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody Product product) {

        Product savedProduct = productService.saveProduct(categoryId, product);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", productMapper.toDTO(savedProduct)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", productMapper.toDTOList(products)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product fetched successfully", productMapper.toDTO(product)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", productMapper.toDTO(updatedProduct)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
