package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

