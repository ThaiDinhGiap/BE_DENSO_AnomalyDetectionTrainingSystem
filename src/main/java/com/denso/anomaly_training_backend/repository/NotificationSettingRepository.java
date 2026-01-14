package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}

