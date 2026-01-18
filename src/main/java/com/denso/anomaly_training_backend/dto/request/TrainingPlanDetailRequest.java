package com.denso.anomaly_training_backend.dto.request;

import com.denso.anomaly_training_backend.enums.TrainingPlanDetailStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TrainingPlanDetailRequest {
    // Không cần ID vì ta sẽ xóa đi tạo lại

    @Schema(description = "ID Nhân viên", example = "101")
    private Long employeeId;

    @Schema(description = "ID Công đoạn", example = "5")
    private Long processId;

    @Schema(description = "Tháng cột (để FE dễ render)", example = "2016-10-01")
    private LocalDate targetMonth;

    @Schema(description = "Ngày dự kiến đào tạo", example = "2016-10-04")
    private LocalDate plannedDate;

    // FE gửi trạng thái: PENDING (mặc định), SICK_LEAVE (nghỉ ốm)...
    @Schema(description = "Trạng thái kết quả", example = "PENDING")
    private TrainingPlanDetailStatus resultStatus;

    @Schema(description = "Ghi chú", example = "Lịch bù")
    private String note;
}