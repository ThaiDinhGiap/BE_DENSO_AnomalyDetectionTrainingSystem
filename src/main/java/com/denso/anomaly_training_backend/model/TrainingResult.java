package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "training_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_tl")
    private User createdByTl;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_by_fi")
    private User confirmByFi;

    @Column(name = "confirm_at_fi")
    private Instant confirmAtFi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_sv")
    private User verifiedBySv;

    @Column(name = "verified_at_sv")
    private Instant verifiedAtSv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_manager")
    private User approvedByManager;

    @Column(name = "approved_at_manager")
    private Instant approvedAtManager;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TrainingResultStatus status = TrainingResultStatus.DRAFT;

    @Column(name = "current_version")
    private Integer currentVersion = 1;

    @Column(name = "last_reject_reason", columnDefinition = "text")
    private String lastRejectReason;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
