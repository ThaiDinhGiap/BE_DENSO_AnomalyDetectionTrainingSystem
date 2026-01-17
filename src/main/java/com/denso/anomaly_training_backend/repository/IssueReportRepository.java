package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.enums.IssueReportStatus;
import com.denso.anomaly_training_backend.model.IssueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {

    @Query("SELECT t FROM IssueReport t " +
            "WHERE t.status = :status " +
            "AND t.updatedAt < :threshold " +
            "AND t.deleteFlag = false")
    List<IssueReport> findByStatusAndUpdatedAtBefore(
            @Param("status") IssueReportStatus status,
            @Param("threshold") Instant threshold
    );
}

