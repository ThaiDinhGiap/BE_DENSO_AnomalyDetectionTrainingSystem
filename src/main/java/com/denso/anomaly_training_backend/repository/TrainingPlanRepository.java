package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.enums.TrainingPlanStatus;
import com.denso.anomaly_training_backend.model.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {

    @Query("SELECT t FROM TrainingPlan t " +
            "WHERE t.status = :status " +
            "AND t.updatedAt < :threshold " +
            "AND t.deleteFlag = false")
    List<TrainingPlan> findByStatusAndUpdatedAtBefore(
            @Param("status") TrainingPlanStatus status,
            @Param("threshold") Instant threshold
    );
}

