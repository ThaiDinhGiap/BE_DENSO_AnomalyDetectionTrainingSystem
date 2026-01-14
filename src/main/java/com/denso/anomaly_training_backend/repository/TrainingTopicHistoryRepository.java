package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingTopicHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingTopicHistoryRepository extends JpaRepository<TrainingTopicHistory, Long> {
}

