package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessRepository extends JpaRepository<Process, Long> {
    boolean existsByCode(String code);

    List<Process> findByGroupId(Long groupId);
}

