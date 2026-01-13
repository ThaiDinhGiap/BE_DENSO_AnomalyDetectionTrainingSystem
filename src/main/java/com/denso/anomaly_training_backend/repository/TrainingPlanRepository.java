package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
}

