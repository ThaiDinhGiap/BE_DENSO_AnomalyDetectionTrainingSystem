package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingTopicHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTopicHistoryRepository extends JpaRepository<TrainingTopicHistory, Long> {
}

