package com.denso.anomaly_training_backend.service;

import com.denso.anomaly_training_backend.dto.request.TrainingPlanRequest;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanInitDataResponse;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanResponse;

public interface TrainingPlanService {

    /**
     * Lấy dữ liệu nguồn (Employees, Processes) dựa trên Group ID
     * để chuẩn bị tạo Plan mới.
     * @param groupId ID của dây chuyền
     * @return DTO chứa full info cần thiết
     */
    TrainingPlanInitDataResponse getInitializationData(Long groupId);
    public Long submitPlan(TrainingPlanRequest request);
    public Long saveDraft(TrainingPlanRequest request);
    TrainingPlanResponse getTrainingPlanById(Long id);
}