package com.kutub.ecommerce.ecommerce_api.controller;

import com.kutub.ecommerce.ecommerce_api.dto.ApiResponse;
import com.kutub.ecommerce.ecommerce_api.dto.ProductDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import com.kutub.ecommerce.ecommerce_api.mapper.ProductMapper;
import com.kutub.ecommerce.ecommerce_api.service.FileService;
import com.kutub.ecommerce.ecommerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<Product> productPage = productService.getAllProducts(page, size, sortBy, keyword);
        List<ProductDTO> dtos = productMapper.toDTOList(productPage.getContent());
        String message = String.format("Showing %d of %d products", productPage.getNumberOfElements(), productPage.getTotalElements());
        return ResponseEntity.ok(ApiResponse.success(message, dtos));
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(categoryId, product);
        return ResponseEntity.ok(ApiResponse.success("Product added successfully", productMapper.toDTO(savedProduct)));
    }

    @PostMapping("/image/upload/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> uploadImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {
        
        Product product = productService.getProductById(productId);
        
        // পুরনো ছবি থাকলে ডিলিট করে দিচ্ছি (Professional Practice)
        if (product.getImageName() != null) {
            File oldFile = new File(path + product.getImageName());
            if (oldFile.exists()) oldFile.delete();
        }

        String fileName = fileService.uploadImage(path, image);
        product.setImageName(fileName);
        Product updatedProduct = productService.updateProduct(productId, product);
        
        return ResponseEntity.ok(ApiResponse.success("Image updated successfully", productMapper.toDTO(updatedProduct)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
