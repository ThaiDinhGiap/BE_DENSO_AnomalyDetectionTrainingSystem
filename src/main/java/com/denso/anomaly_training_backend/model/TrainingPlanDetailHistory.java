package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_plan_detail_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlanDetailHistory {
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

    @Column(name = "result_status")
    private String resultStatus;
}
