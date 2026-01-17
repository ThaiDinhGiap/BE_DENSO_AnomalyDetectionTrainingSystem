package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.enums.TrainingResultStatus;
import com.denso.anomaly_training_backend.model.TrainingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TrainingResultRepository extends JpaRepository<TrainingResult, Long> {

    @Query("SELECT t FROM TrainingResult t " +
            "WHERE t.status = :status " +
            "AND t.updatedAt < :threshold " +
            "AND t.deleteFlag = false")
    List<TrainingResult> findByStatusAndUpdatedAtBefore(
            @Param("status") TrainingResultStatus status,
            @Param("threshold") Instant threshold
    );
}

