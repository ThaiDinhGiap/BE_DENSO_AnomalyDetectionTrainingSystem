package com.denso.anomaly_training_backend.model;

import com.denso.anomaly_training_backend.enums.IssueReportStatus;
import com.denso.anomaly_training_backend.enums.RejectLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class IssueReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_tl", nullable = false)
    User createdByTl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_sv")
    User verifiedBySv;

    @Column(name = "verified_at_sv")
    LocalDateTime verifiedAtSv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_manager")
    User approvedByManager;

    @Column(name = "approved_at_manager")
    LocalDateTime approvedAtManager;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    IssueReportStatus status = IssueReportStatus.DRAFT;

    @Column(name = "rejected_by")
    Long rejectedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "reject_level")
    RejectLevel rejectLevel;

    @Column(name = "reject_reason", columnDefinition = "text")
    String rejectReason;

    @Column(name = "current_version")
    Integer currentVersion = 1;

    @Column(name = "last_reject_reason", columnDefinition = "text")
    String lastRejectReason;
}
