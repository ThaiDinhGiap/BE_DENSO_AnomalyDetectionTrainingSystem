package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_topics_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTopicHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_topic_id", nullable = false)
    private Long trainingTopicId;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "created_by_tl")
    private Long createdByTl;

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

    @Column(name = "recorded_at")
    private java.time.Instant recordedAt;
}

