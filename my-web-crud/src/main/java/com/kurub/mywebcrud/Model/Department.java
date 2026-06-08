package com.kurub.mywebcrud.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department Name is required")
    @Column(nullable = false, unique = true)
    private String name; // e.g., Science, Arts, Commerce

    private String description;

    @OneToMany(mappedBy = "departmentObj")
    private List<Teacher> teachers;
}
