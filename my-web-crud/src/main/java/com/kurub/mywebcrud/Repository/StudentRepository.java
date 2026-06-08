package com.kurub.mywebcrud.Repository;

import com.kurub.mywebcrud.Model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Student> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s.rollNumber FROM Student s ORDER BY s.id DESC LIMIT 1")
    Optional<String> findLastRollNumber();
}
