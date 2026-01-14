package com.denso.anomaly_training_backend.dto.request;

import com.denso.anomaly_training_backend.model.ProcessClassification;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProcessRequest {
    private Long groupId;
    private String code;
    private String name;

    private String description;
    private ProcessClassification classification;
    private BigDecimal standardTimeJt;
}