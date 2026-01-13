package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_skills", uniqueConstraints = {
        @UniqueConstraint(name = "unique_skill", columnNames = {"employee_id", "process_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id")
    private ProcessEntity process;

    @Column(name = "certification_date")
    private LocalDate certificationDate;

    @Column(name = "is_qualified")
    @Builder.Default
    private Boolean isQualified = true;
}
