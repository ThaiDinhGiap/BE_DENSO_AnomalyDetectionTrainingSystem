package com.denso.anomaly_training_backend.service;

import com.denso.anomaly_training_backend.dto.request.EmployeeRequest;
import com.denso.anomaly_training_backend.dto.response.EmployeeResponse;
import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id); // Soft delete

    EmployeeResponse getEmployeeById(Long id);

    List<EmployeeResponse> getAllEmployees();
    // Tìm theo Team

    List<EmployeeResponse> getEmployeesByTeam(Long teamId);
}