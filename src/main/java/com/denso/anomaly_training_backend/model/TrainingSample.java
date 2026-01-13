package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "training_samples")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "past_defect_id", unique = true)
    private PastDefect pastDefect;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id")
    private ProcessEntity process;

    @Lob
    private String description;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }
}
