package com.denso.anomaly_training_backend.model;

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
public class IssueReport extends BaseEntity{
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

    @Column(name = "status")
    String status;

    @Column(name = "rejected_by")
    Long rejectedBy;

    @Column(name = "reject_level")
    String rejectLevel;

    @Column(name = "reject_reason", columnDefinition = "text")
    String rejectReason;

    @Column(name = "current_version")
    Integer currentVersion;

    @Column(name = "last_reject_reason", columnDefinition = "text")
    String lastRejectReason;

}
