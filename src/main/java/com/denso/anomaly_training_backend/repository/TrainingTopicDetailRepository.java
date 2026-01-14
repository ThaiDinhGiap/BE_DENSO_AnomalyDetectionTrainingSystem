package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingTopicDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTopicDetailRepository extends JpaRepository<TrainingTopicDetail, Long> {
}

