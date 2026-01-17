package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.enums.TrainingResultDetailStatus;
import com.denso.anomaly_training_backend.model.TrainingResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TrainingResultDetailRepository extends JpaRepository<TrainingResultDetail, Long> {

    @Query("SELECT t FROM TrainingResultDetail t " +
            "WHERE t.status = :status " +
            "AND t.updatedAt < :threshold " +
            "AND t.deleteFlag = false")
    List<TrainingResultDetail> findByStatusAndUpdatedAtBefore(
            @Param("status") TrainingResultDetailStatus status,
            @Param("threshold") Instant threshold
    );
}

