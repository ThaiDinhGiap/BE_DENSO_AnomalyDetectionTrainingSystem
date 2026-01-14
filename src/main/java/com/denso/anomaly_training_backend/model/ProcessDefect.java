package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "process_defects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessDefect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_detail_id", nullable = false)
    private Long issueDetailId;

    @Column(name = "defect_description", nullable = false, columnDefinition = "text")
    private String defectDescription;

    @Column(name = "process_id", nullable = false)
    private Long processId;

    @Column(name = "detected_date", nullable = false)
    private LocalDate detectedDate;

    @Column(name = "is_escaped")
    private Boolean isEscaped = false;

    @Column(columnDefinition = "text")
    private String note;

    @Column(name = "created_at")
    private java.time.Instant createdAt;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}
