package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "training_plan_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingPlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id")
    private TrainingPlan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "training_sample_id")
    private TrainingSample trainingSample;

    @Column(name = "planned_date")
    private LocalDate plannedDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PlanDetailStatus status = PlanDetailStatus.PLANNED;

    @Lob
    private String note;
}
