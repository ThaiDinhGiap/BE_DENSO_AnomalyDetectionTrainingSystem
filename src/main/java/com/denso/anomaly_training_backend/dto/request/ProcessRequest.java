package com.denso.anomaly_training_backend.dto.request;

import com.denso.anomaly_training_backend.enums.ProcessClassification;
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