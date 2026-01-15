package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "training_result_history")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrainingResultHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_result_id", nullable = false)
    private Long trainingResultId;

    @Column(name = "title")
    private String title;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "year")
    private Integer year;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "verified_by_sv")
    private Long verifiedBySv;

    @Column(name = "rejected_by")
    private Long rejectedBy;

    @Column(name = "reject_level")
    private String rejectLevel;

    @Column(name = "reject_reason", columnDefinition = "text")
    private String rejectReason;
}
