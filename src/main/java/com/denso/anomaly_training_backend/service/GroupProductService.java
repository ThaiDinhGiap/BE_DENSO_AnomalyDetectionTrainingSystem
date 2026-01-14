package com.denso.anomaly_training_backend.service;

import com.denso.anomaly_training_backend.dto.request.GroupProductRequest;
import com.denso.anomaly_training_backend.dto.response.GroupProductResponse;
import java.util.List;

public interface GroupProductService {
    GroupProductResponse createProduct(GroupProductRequest request);
    GroupProductResponse updateProduct(Long id, GroupProductRequest request);
    void deleteProduct(Long id); // Soft delete
    GroupProductResponse getProductById(Long id);
    List<GroupProductResponse> getAllProducts();

    // Tìm sản phẩm theo nhóm
    List<GroupProductResponse> getProductsByGroup(Long groupId);
}