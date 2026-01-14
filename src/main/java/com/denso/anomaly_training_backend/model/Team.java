package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Team extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_leader_id")
    private User teamLeader;

//    @Column(name = "created_at", updatable = false)
//    private Instant createdAt;
//
//    @PrePersist
//    protected void onCreate() { createdAt = Instant.now(); }
}

