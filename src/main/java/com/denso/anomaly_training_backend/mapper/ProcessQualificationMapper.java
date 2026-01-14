package com.denso.anomaly_training_backend.mapper;

import com.denso.anomaly_training_backend.dto.request.ProcessQualificationRequest;
import com.denso.anomaly_training_backend.dto.response.ProcessQualificationResponse;
import com.denso.anomaly_training_backend.model.Employee;
import com.denso.anomaly_training_backend.model.ProcessEntity;
import com.denso.anomaly_training_backend.model.ProcessQualification;
import com.denso.anomaly_training_backend.repository.EmployeeRepository;
import com.denso.anomaly_training_backend.repository.ProcessRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProcessQualificationMapper {

    @Autowired
    protected EmployeeRepository employeeRepository;
    @Autowired
    protected ProcessRepository processRepository;

    // 1. Entity -> DTO (Phức tạp: Cần map ID sang Name/Code)
    @Mapping(target = "employeeName", source = "employeeId", qualifiedByName = "getEmployeeName")
    @Mapping(target = "employeeCode", source = "employeeId", qualifiedByName = "getEmployeeCode")
    @Mapping(target = "processName", source = "processId", qualifiedByName = "getProcessName")
    @Mapping(target = "processCode", source = "processId", qualifiedByName = "getProcessCode")
    public abstract ProcessQualificationResponse toDTO(ProcessQualification entity);

    // 2. DTO -> Entity (Create) - Đơn giản vì Entity dùng ID trực tiếp
    // isQualified mặc định là true nếu null
    public abstract ProcessQualification toEntity(ProcessQualificationRequest dto);

    // 3. Update Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget ProcessQualification entity, ProcessQualificationRequest dto);

    // --- Helper Methods ---

    @Named("getEmployeeName")
    String getEmployeeName(Long id) {
        if (id == null) return null;
        return employeeRepository.findById(id).map(Employee::getFullName).orElse("Unknown");
    }

    @Named("getEmployeeCode")
    String getEmployeeCode(Long id) {
        if (id == null) return null;
        return employeeRepository.findById(id).map(Employee::getEmployeeCode).orElse("Unknown");
    }

    @Named("getProcessName")
    String getProcessName(Long id) {
        if (id == null) return null;
        return processRepository.findById(id).map(ProcessEntity::getName).orElse("Unknown");
    }

    @Named("getProcessCode")
    String getProcessCode(Long id) {
        if (id == null) return null;
        return processRepository.findById(id).map(ProcessEntity::getCode).orElse("Unknown");
    }
}