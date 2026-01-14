package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_queue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_user_id", nullable = false)
    private Long recipientUserId;

    @Column(name = "cc_list")
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

    @Column(name = "status")
    private String status;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "scheduled_at")
    private java.time.Instant scheduledAt;

    @Column(name = "sent_at")
    private java.time.Instant sentAt;

    @Column(name = "created_at")
    private java.time.Instant createdAt;
}

