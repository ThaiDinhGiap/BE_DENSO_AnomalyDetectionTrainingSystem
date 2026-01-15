package com.denso.anomaly_training_backend.mapper;

import com.denso.anomaly_training_backend.dto.response.DefectTrainingContentResponse;
import com.denso.anomaly_training_backend.model.DefectTrainingContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DefectTrainingContentMapper {
    DefectTrainingContentMapper INSTANCE = Mappers.getMapper(DefectTrainingContentMapper.class);

    @Mapping(target = "trainingTopicDetailId", source = "trainingTopicDetail.id")
    @Mapping(target = "processDefectId", source = "processDefect.id")
    DefectTrainingContentResponse toResponse(DefectTrainingContent entity);
}
