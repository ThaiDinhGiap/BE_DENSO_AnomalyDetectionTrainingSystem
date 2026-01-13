package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {
}
