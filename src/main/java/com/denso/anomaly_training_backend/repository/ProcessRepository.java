package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {
    boolean existsByCode(String code);

    List<ProcessEntity> findByGroupId(Long groupId);
}

