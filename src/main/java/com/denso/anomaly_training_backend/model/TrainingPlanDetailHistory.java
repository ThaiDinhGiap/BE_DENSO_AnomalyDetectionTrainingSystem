package com.denso.anomaly_training_backend.model;

import com.denso.anomaly_training_backend.enums.TrainingPlanDetailResultStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "training_plan_detail_history")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrainingPlanDetailHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_plan_history_id", nullable = false)
    private Long trainingPlanHistoryId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "process_id")
    private Long processId;

    @Column(name = "target_month")
    private java.time.LocalDate targetMonth;

    @Column(name = "planned_date")
    private java.time.LocalDate plannedDate;

    @Column(name = "actual_date")
    private java.time.LocalDate actualDate;

    @Column(name = "note", columnDefinition = "text")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status")
    private TrainingPlanDetailResultStatus resultStatus;
}
