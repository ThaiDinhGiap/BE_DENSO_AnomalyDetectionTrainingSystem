package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.PastDefect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PastDefectRepository extends JpaRepository<PastDefect, Long> {
}

