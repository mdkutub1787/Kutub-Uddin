package com.kutub.ecommerce.ecommerce_api.repository;

import com.kutub.ecommerce.ecommerce_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);

    // ৫. প্রফেশনাল কুয়েরি: ইউজারনেম অথবা ইমেইল যেকোনো একটি দিয়ে ইউজার খোঁজা (লগইনের জন্য সেরা)
    @Query("SELECT u FROM User u WHERE u.username = :query OR u.email = :query")
    Optional<User> findByUsernameOrEmail(@Param("query") String query);
}
