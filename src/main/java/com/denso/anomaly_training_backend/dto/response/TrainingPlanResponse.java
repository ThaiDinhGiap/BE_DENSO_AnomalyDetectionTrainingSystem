package com.denso.anomaly_training_backend.dto.response;

import com.denso.anomaly_training_backend.enums.TrainingPlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TrainingPlanResponse {

    @Schema(description = "ID của kế hoạch", example = "10")
    private Long id;

    @Schema(description = "Tiêu đề", example = "Kế hoạch đào tạo Line Vỏ Q4/2016")
    private String title;

    @Schema(description = "Trạng thái hiện tại (DRAFT: Cho sửa, APPROVED: Chỉ xem)", example = "DRAFT")
    private TrainingPlanStatus status;

    @Schema(description = "Phiên bản chỉnh sửa", example = "1")
    private Integer currentVersion;

    // --- Thông tin thời gian ---
    @Schema(description = "Tháng bắt đầu", example = "2016-10-01")
    private LocalDate monthStart;

    @Schema(description = "Tháng kết thúc", example = "2016-12-31")
    private LocalDate monthEnd;

    // --- Thông tin Group ---
    @Schema(description = "ID Dây chuyền", example = "1")
    private Long groupId;

    @Schema(description = "Tên Dây chuyền", example = "Line Sleeve 7")
    private String groupName;

    // --- Thông tin Người duyệt (Hiển thị tên) ---
    @Schema(description = "ID Supervisor", example = "50")
    private Long verifiedBySvId;

    @Schema(description = "Tên Supervisor", example = "Nguyễn Văn SV")
    private String verifiedBySvName;

    @Schema(description = "ID Manager", example = "51")
    private Long approvedByManagerId;

    @Schema(description = "Tên Manager", example = "Trần Văn MNG")
    private String approvedByManagerName;

    // --- Chi tiết ---
    @Schema(description = "Danh sách chi tiết (Flat List - Frontend cần group theo EmployeeId để vẽ bảng)")
    private List<TrainingPlanDetailResponse> details;
}