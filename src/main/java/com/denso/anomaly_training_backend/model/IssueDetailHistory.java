package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "issue_detail_history")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class IssueDetailHistory extends BaseEntity {
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
