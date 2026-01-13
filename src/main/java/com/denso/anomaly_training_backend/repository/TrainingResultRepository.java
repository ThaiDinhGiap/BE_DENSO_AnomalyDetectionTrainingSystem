package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingResultRepository extends JpaRepository<TrainingResult, Long> {
}

