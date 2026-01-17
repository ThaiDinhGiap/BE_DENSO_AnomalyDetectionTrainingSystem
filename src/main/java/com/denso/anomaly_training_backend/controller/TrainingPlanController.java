package com.denso.anomaly_training_backend.controller;

import com.denso.anomaly_training_backend.dto.request.TrainingPlanRequest;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanInitDataResponse;
import com.denso.anomaly_training_backend.dto.response.TrainingPlanResponse;
import com.denso.anomaly_training_backend.service.TrainingPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/training-plans")
@RequiredArgsConstructor
@Tag(name = "01. Training Plan Creation", description = "API liên quan đến việc Tạo mới và Lập kế hoạch")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    // ==================================================================================
    // 1. GET INIT DATA
    // ==================================================================================
    @Operation(
            summary = "Lấy dữ liệu khởi tạo (Nhân viên, Công đoạn)",
            description = "API này dùng khi User vào màn hình 'Tạo kế hoạch'. Nó trả về danh sách nhân viên (theo Team thuộc Group) và danh sách công đoạn để vẽ bảng matrix."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy dữ liệu thành công",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TrainingPlanInitDataResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy Dây chuyền (Group ID sai)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống nội bộ", content = @Content)
    })
    @GetMapping("/init-data/{groupId}")
    public ResponseEntity<TrainingPlanInitDataResponse> getInitData(
            @Parameter(description = "ID của Dây chuyền (Group) cần lập kế hoạch", required = true, example = "1")
            @PathVariable Long groupId) {

        TrainingPlanInitDataResponse response = trainingPlanService.getInitializationData(groupId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/draft")
    @Operation(summary = "Lưu nháp (Save Draft)",
            description = "Lưu lại bảng dữ liệu hiện tại. Trạng thái kế hoạch sẽ là DRAFT. Không bắt buộc nhập người duyệt.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lưu thành công, trả về ID kế hoạch"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy ID tham chiếu (Group, User...)")
    })
    public ResponseEntity<Long> saveDraft(@RequestBody TrainingPlanRequest request) {
        return ResponseEntity.ok(trainingPlanService.saveDraft(request));
    }
// lấy json để test thì vào docs tab7 nhé
    @PostMapping("/submit")
    @Operation(summary = "Gửi duyệt (Submit Plan)",
            description = "Lưu và chuyển trạng thái sang WAITING_SV. Bắt buộc phải chọn SV và Manager.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gửi duyệt thành công"),
            @ApiResponse(responseCode = "400", description = "Thiếu thông tin người duyệt hoặc danh sách rỗng")
    })
    public ResponseEntity<Long> submitPlan(@RequestBody TrainingPlanRequest request) {
        return ResponseEntity.ok(trainingPlanService.submitPlan(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết kế hoạch (Get Detail)",
            description = "Lấy toàn bộ thông tin Header và danh sách chi tiết để hiển thị lên bảng (Matrix).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy kế hoạch với ID cung cấp")
    })
    public ResponseEntity<TrainingPlanResponse> getTrainingPlan(
            @Parameter(description = "ID của kế hoạch cần xem", example = "10")
            @PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanService.getTrainingPlanById(id));
    }
}