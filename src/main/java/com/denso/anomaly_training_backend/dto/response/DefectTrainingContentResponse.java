package com.denso.anomaly_training_backend.dto.response;

import lombok.Data;

@Data
public class DefectTrainingContentResponse {
    Long id;
    Long trainingTopicDetailId;
    Long processDefectId;
    String categoryName;
    String trainingSample;
    String trainingDetail;
}
