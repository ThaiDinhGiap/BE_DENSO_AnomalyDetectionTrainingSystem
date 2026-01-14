package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "defect_training_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefectTrainingContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_topic_detail_id", nullable = false)
    private Long trainingTopicDetailId;

    @Column(name = "process_defect_id", nullable = false)
    private Long processDefectId;

    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "training_sample")
    private String trainingSample;

    @Column(name = "training_detail", nullable = false)
    private String trainingDetail;

    @Column(name = "created_at")
    private java.time.Instant createdAt;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}

