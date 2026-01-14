package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_topic_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTopicDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "process_defect_id")
    private Long processDefectId;

    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "training_sample", columnDefinition = "text")
    private String trainingSample;

    @Column(name = "training_detail", columnDefinition = "text", nullable = false)
    private String trainingDetail;

    @Column(name = "created_at")
    private java.time.Instant createdAt;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}
