package com.denso.anomaly_training_backend.mapper;

import com.denso.anomaly_training_backend.dto.request.ProcessRequest;
import com.denso.anomaly_training_backend.dto.response.ProcessResponse;
import com.denso.anomaly_training_backend.model.Group;
import com.denso.anomaly_training_backend.model.ProcessEntity;
import com.denso.anomaly_training_backend.repository.GroupRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProcessMapper {

    @Autowired
    protected GroupRepository groupRepository;

    // 1. Entity -> DTO
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "groupName", source = "group.name")
    public abstract ProcessResponse toDTO(ProcessEntity entity);

    // 2. DTO -> Entity (Create)
    @Mapping(target = "group", source = "groupId", qualifiedByName = "mapGroupById")
    // Nếu classification null, Entity sẽ dùng @Builder.Default (C4) nếu dùng Builder đúng cách,
    // hoặc ta có thể set default ở đây nếu cần.
    public abstract ProcessEntity toEntity(ProcessRequest dto);

    // 3. Update Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "group", source = "groupId", qualifiedByName = "mapGroupById")
    public abstract void updateEntity(@MappingTarget ProcessEntity entity, ProcessRequest dto);

    // --- Helper Method ---
    @Named("mapGroupById")
    Group mapGroupById(Long id) {
        if (id == null) return null;
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found id: " + id));
    }
}