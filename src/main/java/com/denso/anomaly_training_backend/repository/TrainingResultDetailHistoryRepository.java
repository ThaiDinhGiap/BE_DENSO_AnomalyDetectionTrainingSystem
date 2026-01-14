package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingResultDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingResultDetailHistoryRepository extends JpaRepository<TrainingResultDetailHistory, Long> {
}

