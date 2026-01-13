package com.denso.anomaly_training_backend.repository;

import com.denso.anomaly_training_backend.model.GroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupProductRepository extends JpaRepository<GroupProduct, Long> {
}

