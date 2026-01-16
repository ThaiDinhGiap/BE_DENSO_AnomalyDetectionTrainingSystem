package com.denso.anomaly_training_backend.model;

import com.denso.anomaly_training_backend.enums.TrainingResultDetailStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "training_result_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrainingResultDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_result_id", nullable = false)
    private Long trainingResultId;

    @Column(name = "training_plan_detail_id", nullable = false)
    private Long trainingPlanDetailId;

    @Column(name = "actual_date", nullable = false)
    private LocalDate actualDate;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "training_topic_id")
    private Long trainingTopicId;

    @Column(name = "time_in", nullable = false)
    private LocalTime timeIn;

    @Column(name = "time_out", nullable = false)
    private LocalTime timeOut;

    @Column(name = "signature_pro_in")
    private Long signatureProIn;

    @Column(name = "signature_fi_in")
    private Long signatureFiIn;

    @Column(name = "signature_pro_out")
    private Long signatureProOut;

    @Column(name = "signature_fi_out")
    private Long signatureFiOut;

    @Column(name = "detection_time")
    private Integer detectionTime;

    @Column(name = "is_pass")
    private Boolean isPass;

    @Column(name = "remedial_action", columnDefinition = "text")
    private String remedialAction;

    @Column(name = "sv_confirmation")
    private Long svConfirmation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TrainingResultDetailStatus status = TrainingResultDetailStatus.DRAFT;

    @Column(name = "current_version")
    private Integer currentVersion = 1;

    @Column(name = "last_reject_reason", columnDefinition = "text")
    private String lastRejectReason;
}

