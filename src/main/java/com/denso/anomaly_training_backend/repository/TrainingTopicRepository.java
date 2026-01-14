package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTopicRepository extends JpaRepository<TrainingTopic, Long> {
}

