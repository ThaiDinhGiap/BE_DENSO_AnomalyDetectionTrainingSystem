package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "past_defects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PastDefect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id")
    private ProcessEntity process;

    @Lob
    @Column(name = "defect_description", nullable = false)
    private String defectDescription;

    @Column(name = "detected_date")
    private LocalDate detectedDate;

    @Lob
    private String rootCause;

    @Lob
    private String countermeasure;

    @Column(name = "is_escaped")
    @Builder.Default
    private Boolean isEscaped = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PlanStatus status = PlanStatus.DRAFT;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }
}
