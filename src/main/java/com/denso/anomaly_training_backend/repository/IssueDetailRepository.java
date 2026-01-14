package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.IssueDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueDetailRepository extends JpaRepository<IssueDetail, Long> {
}

