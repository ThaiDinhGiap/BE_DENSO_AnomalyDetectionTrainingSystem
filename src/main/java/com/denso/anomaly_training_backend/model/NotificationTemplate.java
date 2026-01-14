package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate {
    @Id
    @Column(length = 50)
    private String code;

    @Column(name = "subject_template", nullable = false, length = 200)
    private String subjectTemplate;

    @Column(name = "body_template", nullable = false, columnDefinition = "text")
    private String bodyTemplate;

    @Column(length = 500)
    private String description;

    @Column(name = "updated_at")
    private java.time.Instant updatedAt;
}
