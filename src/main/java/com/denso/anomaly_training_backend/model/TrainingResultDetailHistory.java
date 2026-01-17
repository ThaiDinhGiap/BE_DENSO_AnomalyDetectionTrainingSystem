package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "training_result_detail_history")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrainingResultDetailHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_result_detail_id", nullable = false)
    private Long trainingResultDetailId;

    @Column(name = "training_result_history_id", nullable = false)
    private Long trainingResultHistoryId;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
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

    @Column(name = "rejected_by")
    private Long rejectedBy;

    @Column(name = "reject_reason", columnDefinition = "text")
    private String rejectReason;
}
