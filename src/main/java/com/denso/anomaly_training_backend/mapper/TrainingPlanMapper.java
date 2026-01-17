package com.denso.anomaly_training_backend.mapper;

import com.denso.anomaly_training_backend.dto.request.TrainingPlanDetailRequest;
import com.denso.anomaly_training_backend.dto.request.TrainingPlanRequest;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanDetailResponse;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanResponse;
import com.denso.anomaly_training_backend.model.*;
import com.denso.anomaly_training_backend.model.Process; // Import đúng Model Process
import com.denso.anomaly_training_backend.repository.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TrainingPlanMapper {

    @Autowired protected GroupRepository groupRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected EmployeeRepository employeeRepository;
    @Autowired protected ProcessRepository processRepository;

    // --- 1. MAP HEADER ---
    @Mapping(target = "group", source = "groupId", qualifiedByName = "mapGroup")
    @Mapping(target = "verifiedBySv", source = "verifiedBySvId", qualifiedByName = "mapUser")
    @Mapping(target = "approvedByManager", source = "approvedByManagerId", qualifiedByName = "mapUser")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract void updateTrainingPlanFromRequest(TrainingPlanRequest request, @MappingTarget TrainingPlan entity);

    // --- 2. MAP DETAIL ---
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "mapEmployee")
    @Mapping(target = "process", source = "processId", qualifiedByName = "mapProcess")
    @Mapping(target = "trainingPlan", ignore = true)
    // Nếu request không có status -> Mặc định PENDING
    @Mapping(target = "resultStatus", expression = "java(request.getResultStatus() != null ? request.getResultStatus() : com.denso.anomaly_training_backend.enums.TrainingPlanDetailStatus.PENDING)")
    public abstract TrainingPlanDetail toDetailEntity(TrainingPlanDetailRequest request);

    // --- 3. HELPER LOOKUP ---
    @Named("mapGroup")
    protected Group mapGroup(Long id) {
        return id == null ? null : groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found ID: " + id));
    }
    @Named("mapUser")
    protected User mapUser(Long id) {
        return id == null ? null : userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found ID: " + id));
    }
    @Named("mapEmployee")
    protected Employee mapEmployee(Long id) {
        return id == null ? null : employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found ID: " + id));
    }
    @Named("mapProcess")
    protected Process mapProcess(Long id) {
        return id == null ? null : processRepository.findById(id).orElseThrow(() -> new RuntimeException("Process not found ID: " + id));
    }

    // --- 3. MAP ENTITY TO RESPONSE (GET API) ---

    @Mapping(target = "groupName", source = "group.name")
    @Mapping(target = "verifiedBySvName", source = "verifiedBySv.fullName")
    @Mapping(target = "approvedByManagerName", source = "approvedByManager.fullName")
    // Map list details tự động nhờ hàm toDetailResponse bên dưới
    public abstract TrainingPlanResponse toResponse(TrainingPlan entity);

    @Mapping(target = "employeeName", source = "employee.fullName")
    @Mapping(target = "employeeCode", source = "employee.employeeCode")
    @Mapping(target = "processName", source = "process.name")
    public abstract TrainingPlanDetailResponse toDetailResponse(TrainingPlanDetail entity);
}