package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "process_qualifications", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_process", columnNames = {"employee_id", "process_id"})
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProcessQualification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id")
    private Process process;

    @Column(name = "is_qualified")
    @Builder.Default
    private Boolean isQualified = true;

    @Column(name = "certified_date")
    private LocalDate certifiedDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}

