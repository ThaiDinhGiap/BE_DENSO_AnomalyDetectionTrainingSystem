package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "training_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_detail_id")
    private TrainingPlanDetail planDetail;

    @Column(name = "actual_date", nullable = false)
    private LocalDate actualDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_product_id")
    private GroupProduct groupProduct;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "detection_time")
    private Integer detectionTime;

    @Column(name = "is_pass", nullable = false)
    private Boolean isPass;

    @Lob
    private String remedialAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signature_pro_id")
    private User signaturePro;

    @Column(name = "signature_pro_at")
    private Instant signatureProAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signature_fi_id")
    private User signatureFi;

    @Column(name = "signature_fi_at")
    private Instant signatureFiAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_sv")
    private User verifiedBySv;

    @Column(name = "verified_at_sv")
    private Instant verifiedAtSv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_mgr")
    private User approvedByMgr;

    @Column(name = "approved_at_mgr")
    private Instant approvedAtMgr;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TrainingResultStatus status = TrainingResultStatus.DRAFT;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }
}
