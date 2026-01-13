package com.denso.anomaly_training_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_products", uniqueConstraints = {
        @UniqueConstraint(name = "unique_group_prod", columnNames = {"group_id", "product_code"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "product_code", nullable = false)
    private String productCode;
}

