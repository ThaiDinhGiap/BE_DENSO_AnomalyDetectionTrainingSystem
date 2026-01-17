package com.denso.anomaly_training_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TrainingPlanRequest {

    // Nếu có ID -> Update, Không có ID -> Create mới
    @Schema(description = "ID của kế hoạch (Gửi null nếu là tạo mới)", example = "1")
    private Long id;

    @Schema(description = "Tiêu đề", example = "Kế hoạch diễn tập báo cáo bất thường Line Vỏ - Q4/2016")
    private String title;

    @NotNull
    @Schema(description = "ID Dây chuyền (Group)", example = "1")
    private Long groupId;

    // --- Thời gian của kế hoạch (Ví dụ Quý 4: T10 - T12) ---
    @NotNull
    @Schema(description = "Tháng bắt đầu", example = "2016-10-01")
    private LocalDate monthStart;

    @NotNull
    @Schema(description = "Tháng kết thúc", example = "2016-12-31")
    private LocalDate monthEnd;

    // --- Người ký  ---
    @Schema(description = "ID Supervisor (Người kiểm tra)", example = "10")
    private Long verifiedBySvId;

    @Schema(description = "ID Manager (Người duyệt)", example = "11")
    private Long approvedByManagerId;

    // --- Danh sách chi tiết (Các dòng trong Excel) ---
    private List<TrainingPlanDetailRequest> details;
}