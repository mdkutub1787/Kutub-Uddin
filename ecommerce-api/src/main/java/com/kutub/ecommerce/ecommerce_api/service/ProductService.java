package com.kutub.ecommerce.ecommerce_api.service;

import com.kutub.ecommerce.ecommerce_api.entity.Category;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import com.kutub.ecommerce.ecommerce_api.repository.CategoryRepository;
import com.kutub.ecommerce.ecommerce_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * নতুন প্রোডাক্ট সেভ করা। 
     * এখানে আমরা প্রথমে ক্যাটাগরি চেক করি, তারপর প্রোডাক্টের সাথে লিঙ্ক করি।
     */
    public Product saveProduct(Long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        
        product.setCategory(category);
        return productRepository.save(product);
    }

    /**
     * পেজিনেশন এবং সর্টিং সহ সব প্রোডাক্ট দেখা। 
     * এটি অটোমেটিক SQL-এ LIMIT এবং OFFSET যোগ করবে।
     */
    public Page<Product> getAllProducts(int page, int size, String sortBy, String keyword) {
        // সর্টিং সেটআপ (যেমন: দাম অনুযায়ী বা নাম অনুযায়ী)
        Sort sort = Sort.by(sortBy).ascending();
        
        // পেজ রিকোয়েস্ট তৈরি
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // যদি সার্চ কি-ওয়ার্ড থাকে তবে সার্চ করবে, না থাকলে সব ডাটা আনবে
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContaining(keyword, pageable);
        }
        
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete! Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
