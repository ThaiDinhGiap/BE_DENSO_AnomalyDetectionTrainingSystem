package com.denso.anomaly_training_backend.model;

import com.denso.anomaly_training_backend.enums.NotificationQueueStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "notification_queue")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NotificationQueue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_user_id", nullable = false)
    private Long recipientUserId;

    @Column(name = "cc_list", columnDefinition = "text")
    private String ccList;

    @Column(name = "notification_type", length = 50, nullable = false)
    private String notificationType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "related_entity_table", length = 50)
    private String relatedEntityTable;

    @Column(name = "subject", length = 255, nullable = false)
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationQueueStatus status = NotificationQueueStatus.PENDING;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "max_retries")
    private Integer maxRetries = 3;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "scheduled_at")
    private java.time.Instant scheduledAt;

    @Column(name = "sent_at")
    private java.time.Instant sentAt;
}

