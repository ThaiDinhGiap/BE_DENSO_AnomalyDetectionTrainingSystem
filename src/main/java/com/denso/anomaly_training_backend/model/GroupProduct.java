package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "product_group", uniqueConstraints = {
        @UniqueConstraint(name = "uk_group_product", columnNames = {"group_id", "product_code"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "product_code", nullable = false)
    private String productCode;

//    @Column(name = "created_at", updatable = false)
//    private Instant createdAt;
//
//    @PrePersist
//    protected void onCreate() { createdAt = Instant.now(); }
}
