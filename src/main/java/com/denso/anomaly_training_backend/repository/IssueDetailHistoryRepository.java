package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.IssueDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueDetailHistoryRepository extends JpaRepository<IssueDetailHistory, Long> {
}

