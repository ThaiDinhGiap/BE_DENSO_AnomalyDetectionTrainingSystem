package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "process_defects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessDefect extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_detail_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IssueDetail issueDetail;

    @Column(name = "defect_description", nullable = false, columnDefinition = "text")
    String defectDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Process process;

    @Column(name = "detected_date", nullable = false)
    LocalDate detectedDate;

    @Column(name = "is_escaped")
    Boolean isEscaped = false;

    @Column(columnDefinition = "text")
    String note;
}
