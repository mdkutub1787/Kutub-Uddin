package com.kurub.mywebcrud.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room Name is required")
    @Column(nullable = false, unique = true)
    private String name; // e.g., Room 101, Lab A

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    private String type; // e.g., Classroom, Lab, Office
}
