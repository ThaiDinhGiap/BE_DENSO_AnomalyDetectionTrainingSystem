package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.TrainingPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingPlanDetailRepository extends JpaRepository<TrainingPlanDetail, Long> {

    @Query("SELECT t FROM TrainingPlanDetail t " +
            "WHERE t.plannedDate = :plannedDate " +
            "AND t.resultStatus = 'PENDING' " +
            "AND t.deleteFlag = false")
    List<TrainingPlanDetail> findByPlannedDateAndResultStatusPending(@Param("plannedDate") LocalDate plannedDate);

    @Query("SELECT t FROM TrainingPlanDetail t " +
            "WHERE t.plannedDate < :today " +
            "AND t.resultStatus = 'PENDING' " +
            "AND t.deleteFlag = false")
    List<TrainingPlanDetail> findOverdueTrainings(@Param("today") LocalDate today);
}

