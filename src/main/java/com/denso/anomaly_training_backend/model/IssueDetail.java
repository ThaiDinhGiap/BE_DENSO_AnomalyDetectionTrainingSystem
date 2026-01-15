package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "issue_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class IssueDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_report_id")
    private IssueReport issueReport;

    @Column(name = "defect_description", nullable = false, columnDefinition = "text")
    private String defectDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id")
    private Process process;

    @Column(name = "detected_date", nullable = false)
    private LocalDate detectedDate;

    @Column(name = "is_escaped")
    private Boolean isEscaped = false;

    @Column(name = "note", columnDefinition = "text")
    private String note;
}

