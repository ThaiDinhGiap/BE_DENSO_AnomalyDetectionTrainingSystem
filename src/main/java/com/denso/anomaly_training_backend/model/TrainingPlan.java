package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "training_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_sv")
    private User approvedBySv;

    @Column(name = "approved_at_sv")
    private Instant approvedAtSv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_mgr")
    private User approvedByMgr;

    @Column(name = "approved_at_mgr")
    private Instant approvedAtMgr;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PlanStatus status = PlanStatus.DRAFT;

    @Lob
    private String note;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }
}
