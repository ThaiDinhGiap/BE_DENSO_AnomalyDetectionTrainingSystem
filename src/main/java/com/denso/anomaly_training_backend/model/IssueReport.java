package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "issue_report")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class IssueReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_tl", nullable = false)
    private User createdByTl;

    @Column(name = "verified_by_sv")
    private Long verifiedBySv;

    @Column(name = "verified_at_sv")
    private java.time.Instant verifiedAtSv;

    @Column(name = "approved_by_manager")
    private Long approvedByManager;

    @Column(name = "approved_at_manager")
    private java.time.Instant approvedAtManager;

    @Column(name = "status")
    private String status = "DRAFT";

    @Column(name = "rejected_by")
    private Long rejectedBy;

    @Column(name = "reject_level")
    private String rejectLevel;

    @Column(name = "reject_reason", columnDefinition = "text")
    private String rejectReason;

    @Column(name = "current_version")
    private Integer currentVersion = 1;

    @Column(name = "last_reject_reason", columnDefinition = "text")
    private String lastRejectReason;
}
