package com.denso.anomaly_training_backend.mapper;

import com.denso.anomaly_training_backend.dto.request.UserRequest;
import com.denso.anomaly_training_backend.dto.response.UserResponse;
import com.denso.anomaly_training_backend.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toDTO(User user);

    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(UserRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passwordHash", ignore = true)
    void updateEntity(@MappingTarget User user, UserRequest dto);
}