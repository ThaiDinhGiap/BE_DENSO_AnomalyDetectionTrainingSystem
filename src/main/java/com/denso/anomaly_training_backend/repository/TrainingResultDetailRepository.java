package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingResultDetailRepository extends JpaRepository<TrainingResultDetail, Long> {
}

