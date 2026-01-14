package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String> {
}

