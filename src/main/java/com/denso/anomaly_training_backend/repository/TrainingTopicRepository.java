package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.enums.TrainingTopicStatus;
import com.denso.anomaly_training_backend.model.TrainingTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TrainingTopicRepository extends JpaRepository<TrainingTopic, Long> {

    @Query("SELECT t FROM TrainingTopic t " +
            "WHERE t.status = :status " +
            "AND t.updatedAt < :threshold " +
            "AND t.deleteFlag = false")
    List<TrainingTopic> findByStatusAndUpdatedAtBefore(
            @Param("status") TrainingTopicStatus status,
            @Param("threshold") Instant threshold
    );
}

