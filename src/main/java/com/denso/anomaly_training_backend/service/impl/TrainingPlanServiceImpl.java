package com.denso.anomaly_training_backend.service.impl;

import com.denso.anomaly_training_backend.dto.request.TrainingPlanDetailRequest;
import com.denso.anomaly_training_backend.dto.request.TrainingPlanRequest;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanInitDataResponse;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanResponse;
import com.denso.anomaly_training_backend.enums.TrainingPlanStatus;
import com.denso.anomaly_training_backend.mapper.MasterDataTrainingPlanMapper;
import com.denso.anomaly_training_backend.mapper.TrainingPlanMapper;
import com.denso.anomaly_training_backend.model.*;
import com.denso.anomaly_training_backend.model.Process;
import com.denso.anomaly_training_backend.repository.GroupRepository;
import com.denso.anomaly_training_backend.repository.EmployeeRepository;
import com.denso.anomaly_training_backend.repository.ProcessRepository;
import com.denso.anomaly_training_backend.repository.TrainingPlanRepository;
import com.denso.anomaly_training_backend.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final GroupRepository groupRepository;
    private final EmployeeRepository employeeRepository;
    private final ProcessRepository processRepository;
    private final MasterDataTrainingPlanMapper masterDataMapper;
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanMapper trainingPlanMapper;

    @Override
    @Transactional(readOnly = true)
    public TrainingPlanInitDataResponse getInitializationData(Long groupId) {
        // 1. Validate Group
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // 2. Lấy danh sách nhân viên:
        // Logic: Lấy tất cả nhân viên thuộc các Team của Group này và đang ACTIVE
        List<Employee> employees = employeeRepository.findAllActiveByGroupId(
                groupId
        );

        // 3. Lấy danh sách công đoạn
        List<Process> processes = processRepository.findByGroup_IdAndDeleteFlagFalse(groupId);

        // 4. Map & Return
        return TrainingPlanInitDataResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .availableEmployees(masterDataMapper.toEmployeeResponseList(employees))
                .availableProcesses(masterDataMapper.toProcessResponseList(processes))
                .build();
    }
    // =================================================================
    // API 1: LƯU NHÁP (SAVE DRAFT)
    // =================================================================
    @Override
    @Transactional
    public Long saveDraft(TrainingPlanRequest request) {
        // Lưu với trạng thái DRAFT -> Chưa gửi đi đâu cả
        return savePlanInternal(request, TrainingPlanStatus.DRAFT);
    }

    // =================================================================
    // API 2: GỬI DUYỆT (SUBMIT)
    // =================================================================
    @Override
    @Transactional
    public Long submitPlan(TrainingPlanRequest request) {
        // 1. Validate dữ liệu khi Submit
        if (request.getVerifiedBySvId() == null || request.getApprovedByManagerId() == null) {
            throw new RuntimeException("Lỗi: Phải chọn Supervisor và Manager trước khi gửi duyệt!");
        }
        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            throw new RuntimeException("Lỗi: Kế hoạch chưa có nội dung chi tiết!");
        }

        // 2. Lưu và chuyển trạng thái sang WAITING_SV
        return savePlanInternal(request, TrainingPlanStatus.WAITING_SV);
    }

    // =================================================================
    // INTERNAL LOGIC (Dùng chung cho cả Save và Submit)
    // =================================================================
    private Long savePlanInternal(TrainingPlanRequest request, TrainingPlanStatus targetStatus) {
        TrainingPlan trainingPlan;

        // BƯỚC 1: Xác định là Tạo mới hay Update
        if (request.getId() != null) {
            trainingPlan = trainingPlanRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Plan ID: " + request.getId()));

            // Logic bảo vệ: Nếu đã duyệt (APPROVED) thì không được sửa nữa
            if (TrainingPlanStatus.APPROVED.equals(trainingPlan.getStatus())) {
                throw new RuntimeException("Không thể chỉnh sửa kế hoạch đã được phê duyệt!");
            }
        } else {
            trainingPlan = new TrainingPlan();
            trainingPlan.setCurrentVersion(1); // Phiên bản đầu tiên
        }

        // BƯỚC 2: Map dữ liệu Header (Title, Group, SV, Manager...)
        trainingPlanMapper.updateTrainingPlanFromRequest(request, trainingPlan);

        trainingPlan.setStatus(targetStatus);

        // BƯỚC 3: Xử lý Detail
        if (trainingPlan.getDetails() == null) {
            trainingPlan.setDetails(new ArrayList<>());
        } else {
            trainingPlan.getDetails().clear();
        }

        if (request.getDetails() != null) {
            for (TrainingPlanDetailRequest itemReq : request.getDetails()) {
                // Mapper tự động  Employee, Process và map resultStatus
                TrainingPlanDetail detail = trainingPlanMapper.toDetailEntity(itemReq);
                detail.setTrainingPlan(trainingPlan);
                trainingPlan.getDetails().add(detail);
            }
        }

        TrainingPlan savedPlan = trainingPlanRepository.save(trainingPlan);
        return savedPlan.getId();
    }

    // =================================================================
    // API 3: XEM CHI TIẾT (GET BY ID)
    // =================================================================
    @Override
    @Transactional(readOnly = true)
    public TrainingPlanResponse getTrainingPlanById(Long id) {
        // 1. Tìm trong DB
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kế hoạch với ID: " + id));

        // 2. Map sang DTO Response (Bao gồm cả list details)
        return trainingPlanMapper.toResponse(trainingPlan);
    }

}