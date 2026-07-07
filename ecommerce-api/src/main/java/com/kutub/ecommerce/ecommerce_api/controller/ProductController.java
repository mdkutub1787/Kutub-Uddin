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

import com.kutub.ecommerce.ecommerce_api.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody Product product) {
// ...

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

    // ইমেজ আপলোড করার এন্ডপয়েন্ট
    @PostMapping("/image/upload/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {

        Product product = productService.getProductById(productId);
        String fileName = fileService.uploadImage(path, image);
        product.setImageName(fileName);
        Product updatedProduct = productService.updateProduct(productId, product);
        
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", productMapper.toDTO(updatedProduct)));
    }
}
