package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_tl", nullable = false)
    private User createdByTl;

    @Column(name = "created_at")
    private java.time.Instant createdAt;

    @Column(name = "verified_by_sv")
    private Long verifiedBySv;

    @Column(name = "verified_at_sv")
    private java.time.Instant verifiedAtSv;

    @Column(name = "approved_by_manager")
    private Long approvedByManager;

    @Column(name = "approved_at_manager")
    private java.time.Instant approvedAtManager;

    @Column(name = "status")
    private String status;

    @Column(name = "rejected_by")
    private Long rejectedBy;

    @Column(name = "reject_level")
    private String rejectLevel;

    @Column(name = "reject_reason", columnDefinition = "text")
    private String rejectReason;

    @Column(name = "current_version")
    private Integer currentVersion;

    @Column(name = "last_reject_reason", columnDefinition = "text")
    private String lastRejectReason;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}
