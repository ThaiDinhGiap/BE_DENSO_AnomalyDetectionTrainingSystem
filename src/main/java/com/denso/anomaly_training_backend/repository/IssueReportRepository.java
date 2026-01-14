package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.IssueReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {
}

