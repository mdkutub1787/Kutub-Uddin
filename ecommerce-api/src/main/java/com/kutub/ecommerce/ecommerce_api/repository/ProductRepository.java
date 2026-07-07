package com.kutub.ecommerce.ecommerce_api.repository;

import com.kutub.ecommerce.ecommerce_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // নামের অংশ দিয়ে সার্চ (পেজিনেশন সহ)
    // SQL: SELECT * FROM products WHERE name LIKE %?% LIMIT ? OFFSET ?
    Page<Product> findByNameContaining(String name, Pageable pageable);

    // প্রাইস ফিল্টারিং (পেজিনেশন সহ)
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    Page<Product> findByPriceBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);
}
