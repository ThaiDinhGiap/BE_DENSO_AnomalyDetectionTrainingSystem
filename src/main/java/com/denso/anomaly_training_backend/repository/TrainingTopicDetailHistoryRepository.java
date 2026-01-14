package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingTopicDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTopicDetailHistoryRepository extends JpaRepository<TrainingTopicDetailHistory, Long> {
}

