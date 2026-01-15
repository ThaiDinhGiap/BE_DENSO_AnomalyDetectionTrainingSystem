package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "issue_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IssueDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "issue_report_id", nullable = false)
    Long issueReportId;

    @Column(name = "defect_description", nullable = false)
    String defectDescription;

    @Column(name = "process_id", nullable = false)
    Long processId;

    @Column(name = "detected_date", nullable = false)
    LocalDate detectedDate;

    @Column(name = "is_escaped")
    Boolean isEscaped = false;

    @Column(name = "note")
    String note;

    @Column(name = "created_at")
    java.time.Instant createdAt;

    @Column(name = "updated_at")
    java.time.Instant updatedAt;
}

