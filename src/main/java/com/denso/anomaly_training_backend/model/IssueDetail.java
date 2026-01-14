package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "issue_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_report_id", nullable = false)
    private Long issueReportId;

    @Column(name = "defect_description", nullable = false)
    private String defectDescription;

    @Column(name = "process_id", nullable = false)
    private Long processId;

    @Column(name = "detected_date", nullable = false)
    private LocalDate detectedDate;

    @Column(name = "is_escaped")
    private Boolean isEscaped = false;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private java.time.Instant createdAt;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}

