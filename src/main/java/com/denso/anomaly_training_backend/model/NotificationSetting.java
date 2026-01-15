package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "notification_settings")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NotificationSetting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_code")
    private NotificationTemplate notificationTemplate;

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
}

