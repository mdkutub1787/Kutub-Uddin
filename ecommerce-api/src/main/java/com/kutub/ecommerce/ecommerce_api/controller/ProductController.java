package com.kutub.ecommerce.ecommerce_api.controller;

import com.kutub.ecommerce.ecommerce_api.dto.ApiResponse;
import com.kutub.ecommerce.ecommerce_api.dto.ProductDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import com.kutub.ecommerce.ecommerce_api.mapper.ProductMapper;
import com.kutub.ecommerce.ecommerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    /**
     * সব প্রোডাক্ট দেখার এপিআই (Readable URL: /api/products?page=0&size=10)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<Product> productPage = productService.getAllProducts(page, size, sortBy, keyword);
        
        // Entity থেকে DTO-তে কনভার্ট করছি
        List<ProductDTO> dtos = productMapper.toDTOList(productPage.getContent());
        
        // ইউজারকে একটি সুন্দর মেসেজ দিচ্ছি
        String message = String.format("Showing %d of %d total products", 
                productPage.getNumberOfElements(), productPage.getTotalElements());
        
        return ResponseEntity.ok(ApiResponse.success(message, dtos));
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(categoryId, product);
        return ResponseEntity.ok(ApiResponse.success("Product added successfully", productMapper.toDTO(savedProduct)));
    }
}
