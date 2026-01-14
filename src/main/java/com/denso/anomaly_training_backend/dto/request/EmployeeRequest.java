package com.denso.anomaly_training_backend.dto.request;

import com.denso.anomaly_training_backend.model.EmployeeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequest {
    private String employeeCode;

    private String fullName;

    private Long teamId;

    private EmployeeStatus status;
}