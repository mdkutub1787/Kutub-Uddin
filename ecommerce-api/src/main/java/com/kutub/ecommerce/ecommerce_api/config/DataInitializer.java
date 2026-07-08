package com.kutub.ecommerce.ecommerce_api.config;

import com.kutub.ecommerce.ecommerce_api.entity.Category;
import com.kutub.ecommerce.ecommerce_api.entity.Product;
import com.kutub.ecommerce.ecommerce_api.entity.Role;
import com.kutub.ecommerce.ecommerce_api.repository.CategoryRepository;
import com.kutub.ecommerce.ecommerce_api.repository.ProductRepository;
import com.kutub.ecommerce.ecommerce_api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, 
                           CategoryRepository categoryRepository, 
                           ProductRepository productRepository) {
        return args -> {
            // ১. রোল তৈরি করা (যদি না থাকে)
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_USER"));
            }
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_ADMIN"));
            }

            // ২. ক্যাটাগরি তৈরি করা (যদি ডাটাবেস খালি থাকে)
            if (categoryRepository.count() == 0) {
                Category electronics = new Category(null, "Electronics", "Gadgets, Phones, Laptops");
                Category fashion = new Category(null, "Fashion", "Clothing, Shoes, Accessories");
                Category home = new Category(null, "Home & Kitchen", "Appliances and Furniture");

                categoryRepository.saveAll(Arrays.asList(electronics, fashion, home));

                // ৩. প্রোডাক্ট তৈরি করা (ক্যাটাগরির সাথে লিঙ্ক করে)
                if (productRepository.count() == 0) {
                    Product p1 = new Product(null, "iPhone 15 Pro", "Latest Apple iPhone", new BigDecimal("120000.00"), 50, null, electronics);
                    Product p2 = new Product(null, "Sony PS5", "Gaming Console", new BigDecimal("65000.00"), 20, null, electronics);
                    
                    Product p3 = new Product(null, "Levi's 501", "Original Fit Jeans", new BigDecimal("4500.00"), 100, null, fashion);
                    Product p4 = new Product(null, "Nike Air Max", "Comfortable Running Shoes", new BigDecimal("12000.00"), 30, null, fashion);
                    
                    Product p5 = new Product(null, "Samsung Microwave", "Smart Inverter Oven", new BigDecimal("15000.00"), 15, null, home);
                    
                    productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
                }
            }
            
            System.out.println("--- Sample Data Initialized Successfully ---");
        };
    }
}
