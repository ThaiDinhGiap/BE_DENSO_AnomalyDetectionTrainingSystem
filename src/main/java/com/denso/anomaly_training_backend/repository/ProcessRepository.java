package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {
}

