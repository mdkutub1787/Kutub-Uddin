package com.kutub.ecommerce.ecommerce_api.controller;

import com.kutub.ecommerce.ecommerce_api.dto.ApiResponse;
import com.kutub.ecommerce.ecommerce_api.dto.CategoryDTO;
import com.kutub.ecommerce.ecommerce_api.entity.Category;
import com.kutub.ecommerce.ecommerce_api.mapper.CategoryMapper;
import com.kutub.ecommerce.ecommerce_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", categoryMapper.toDTO(savedCategory)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories fetched successfully", categoryMapper.toDTOList(categories)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Category fetched successfully", categoryMapper.toDTO(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", categoryMapper.toDTO(updatedCategory)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully with id: " + id, null));
    }
}
