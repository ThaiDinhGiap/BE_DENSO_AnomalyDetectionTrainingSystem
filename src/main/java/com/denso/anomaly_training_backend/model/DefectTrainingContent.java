package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "defect_training_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefectTrainingContent extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "training_topic_detail_id", nullable = false)
    TrainingTopicDetail trainingTopicDetail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_defect_id", nullable = false)
    ProcessDefect processDefect;

    @Column(name = "category_name", nullable = false, length = 200)
    String categoryName;

    @Column(name = "training_sample")
    String trainingSample;

    @Column(name = "training_detail", nullable = false)
    String trainingDetail;


}

