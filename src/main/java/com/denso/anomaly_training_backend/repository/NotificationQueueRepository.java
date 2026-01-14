package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.NotificationQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, Long> {
}

