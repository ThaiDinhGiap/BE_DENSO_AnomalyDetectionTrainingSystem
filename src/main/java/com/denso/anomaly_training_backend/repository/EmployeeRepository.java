package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    List<Employee> findByTeamId(Long teamId);
}

