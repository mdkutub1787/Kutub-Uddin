package com.kurub.mywebcrud.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject Name is required")
    @Size(min = 2, max = 100, message = "Subject Name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name; // e.g., Mathematics, Bengali, English

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
