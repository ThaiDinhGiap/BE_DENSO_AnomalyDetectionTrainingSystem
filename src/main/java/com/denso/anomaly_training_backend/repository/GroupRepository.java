package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}

