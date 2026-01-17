package com.denso.anomaly_training_backend.model;

import com.denso.anomaly_training_backend.enums.ProcessClassification;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "processes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_group_code", columnNames = {"group_id", "code"})
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Process extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    // Persist as TINYINT in DB (migration uses TINYINT). Use explicit columnDefinition and converter.
    @Convert(converter = ProcessClassification.ConverterImpl.class)
    @Column(name = "classification", columnDefinition = "TINYINT", nullable = false)
    @Builder.Default
    private ProcessClassification classification = ProcessClassification.C4;

    @Column(name = "standard_time_jt")
    private BigDecimal standardTimeJt;
}
