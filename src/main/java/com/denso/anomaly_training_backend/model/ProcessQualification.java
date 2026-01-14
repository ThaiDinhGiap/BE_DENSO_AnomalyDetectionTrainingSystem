package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "process_qualifications", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_process", columnNames = {"employee_id", "process_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessQualification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "process_id", nullable = false)
    private Long processId;

    @Column(name = "is_qualified")
    private Boolean isQualified = true;

    @Column(name = "certified_date")
    private LocalDate certifiedDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

//    @Column(name = "created_at")
//    private java.time.Instant createdAt;
//
//    @Column(name = "updated_at")
//    private java.time.Instant updatedAt;
}

