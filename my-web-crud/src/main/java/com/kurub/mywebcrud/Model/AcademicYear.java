package com.kurub.mywebcrud.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "academic_years")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Year Name is required")
    @Column(nullable = false, unique = true)
    private String name; // e.g., 2024, 2024-2025

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isActive = false;
}
