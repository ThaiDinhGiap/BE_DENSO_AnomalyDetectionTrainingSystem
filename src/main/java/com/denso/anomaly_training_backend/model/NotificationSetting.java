package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_code", length = 50, nullable = false)
    private String templateCode;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "remind_before_days")
    private Integer remindBeforeDays = 3;

    @Column(name = "is_persistent")
    private Boolean isPersistent = false;

    @Column(name = "remind_interval_hours")
    private Integer remindIntervalHours = 24;

    @Column(name = "max_reminders")
    private Integer maxReminders = 5;

    @Column(name = "preferred_send_time")
    private java.time.LocalTime preferredSendTime;

    @Column(name = "escalate_after_days")
    private Integer escalateAfterDays;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}

