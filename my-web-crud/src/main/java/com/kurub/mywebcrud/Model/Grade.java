package com.kurub.mywebcrud.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., Class 10-A, Grade 1

    private String section;

    @ManyToOne(fetch = FetchType.EAGER) // Changed to EAGER
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "grade")
    @ToString.Exclude // Exclude from toString to avoid recursion
    private List<Student> students;
}
