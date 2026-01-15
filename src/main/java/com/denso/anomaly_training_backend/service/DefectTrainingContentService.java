package com.denso.anomaly_training_backend.service;

import com.denso.anomaly_training_backend.dto.response.DefectTrainingContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DefectTrainingContentService {
    List<DefectTrainingContentResponse> getAllDefectTrainingContent();
    Page<DefectTrainingContentResponse> getAll(int pageNumber, int pageSize);
}
