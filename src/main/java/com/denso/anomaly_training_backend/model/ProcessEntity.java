package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProcessClassification classification = ProcessClassification._4;

    @Column(name = "standard_time_jt")
    private Integer standardTimeJt;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }
}
