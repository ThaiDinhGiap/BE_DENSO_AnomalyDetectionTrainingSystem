package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.ProcessQualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessQualificationRepository extends JpaRepository<ProcessQualification, Long> {
}

