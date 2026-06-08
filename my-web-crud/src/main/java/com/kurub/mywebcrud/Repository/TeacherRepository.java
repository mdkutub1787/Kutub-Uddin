package com.kurub.mywebcrud.Repository;

import com.kurub.mywebcrud.Model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    @Query("SELECT t FROM Teacher t WHERE " +
           "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.departmentObj.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Teacher> search(@Param("keyword") String keyword, Pageable pageable);
}
