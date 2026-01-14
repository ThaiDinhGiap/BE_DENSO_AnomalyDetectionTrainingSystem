package com.denso.anomaly_training_backend.service;

import com.denso.anomaly_training_backend.dto.request.ProcessQualificationRequest;
import com.denso.anomaly_training_backend.dto.response.ProcessQualificationResponse;
import java.util.List;

public interface ProcessQualificationService {
    ProcessQualificationResponse createQualification(ProcessQualificationRequest request);
    ProcessQualificationResponse updateQualification(Long id, ProcessQualificationRequest request);
    void deleteQualification(Long id);
    ProcessQualificationResponse getQualificationById(Long id);
    List<ProcessQualificationResponse> getAllQualifications();

    List<ProcessQualificationResponse> getByEmployee(Long employeeId);
    List<ProcessQualificationResponse> getByProcess(Long processId);
}