package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingResultHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingResultHistoryRepository extends JpaRepository<TrainingResultHistory, Long> {
}

