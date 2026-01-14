package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_detail_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueDetailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_report_history_id", nullable = false)
    private Long issueReportHistoryId;

    @Column(name = "defect_description", nullable = false, columnDefinition = "text")
    private String defectDescription;

    @Column(name = "process_id", nullable = false)
    private Long processId;

    @Column(name = "detected_date", nullable = false)
    private java.time.LocalDate detectedDate;

    @Column(name = "is_escaped")
    private Boolean isEscaped = false;

    @Column(columnDefinition = "text")
    private String note;
}
