package com.kurub.mywebcrud.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "fee_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @DecimalMin(value = "0.0", message = "Amount must be positive")
    private Double amount;

    private String feeType; // e.g., Monthly Tuition, Exam Fee, Uniform

    private LocalDate paymentDate;

    private String status; // Paid, Pending
}
