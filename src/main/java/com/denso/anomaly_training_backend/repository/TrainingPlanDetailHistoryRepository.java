package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingPlanDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanDetailHistoryRepository extends JpaRepository<TrainingPlanDetailHistory, Long> {
}

