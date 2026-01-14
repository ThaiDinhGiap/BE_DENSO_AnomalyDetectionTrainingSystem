package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.ProcessDefect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessDefectRepository extends JpaRepository<ProcessDefect, Long> {
}

