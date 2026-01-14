package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "processes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    private String code;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    // Persist as TINYINT in DB (migration uses TINYINT). Use explicit columnDefinition and converter.
    @Convert(converter = ProcessClassification.ConverterImpl.class)
    @Column(name = "classification", columnDefinition = "TINYINT")
    @Builder.Default
    private ProcessClassification classification = ProcessClassification.C4;

    @Column(name = "standard_time_jt")
    private BigDecimal standardTimeJt;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }
}
