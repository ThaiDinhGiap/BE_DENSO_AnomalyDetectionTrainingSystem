package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingPlanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanHistoryRepository extends JpaRepository<TrainingPlanHistory, Long> {
}

