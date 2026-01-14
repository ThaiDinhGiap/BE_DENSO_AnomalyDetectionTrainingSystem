-- Flyway migration V2: Refactor issue_detail_id to process_defect_id
-- Reason: Requirement change to link training topics directly to process_defects

SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================================
-- 1. XỬ LÝ BẢNG: TRAINING_TOPIC_DETAIL
-- =========================================================================

-- Bước 1.1: Tìm và xóa Foreign Key cũ (issue_detail_id -> issue_detail)
-- Vì tên FK thường được sinh tự động, ta dùng thủ thuật này để lấy tên và xóa chính xác.
SET @dbName = DATABASE();
SET @fkName = (SELECT CONSTRAINT_NAME
               FROM information_schema.KEY_COLUMN_USAGE
               WHERE TABLE_SCHEMA = @dbName
                 AND TABLE_NAME = 'training_topic_detail'
                 AND COLUMN_NAME = 'issue_detail_id'
                 AND REFERENCED_TABLE_NAME = 'issue_detail'
               LIMIT 1);

-- Tạo câu lệnh DROP FK động
SET @sql = IF(@fkName IS NOT NULL,
              CONCAT('ALTER TABLE training_topic_detail DROP FOREIGN KEY ', @fkName),
              'SELECT "No Foreign Key found to drop"');

-- Thực thi lệnh
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Bước 1.2: Xóa Index cũ (idx_issue_detail)
-- Kiểm tra xem index có tồn tại không trước khi xóa để tránh lỗi (dùng procedure tạm hoặc try-catch nếu MySQL 8, nhưng ở đây dùng DROP IF EXISTS cho gọn)
-- Lưu ý: MySQL cũ không support DROP INDEX IF EXISTS, nên ta cứ chạy thẳng, nếu lỗi thì migration sẽ báo.
-- Tuy nhiên, trong V1 chúng ta đã define rõ tên index là `idx_issue_detail` nên có thể drop trực tiếp.
DROP INDEX idx_issue_detail ON training_topic_detail;

-- Bước 1.3: Đổi tên cột và cập nhật kiểu dữ liệu (CHANGE COLUMN)
ALTER TABLE training_topic_detail
    CHANGE COLUMN issue_detail_id process_defect_id BIGINT COMMENT 'Link to process_defect if applicable';

-- Bước 1.4: Tạo Foreign Key mới trỏ sang process_defects
ALTER TABLE training_topic_detail
    ADD CONSTRAINT fk_ttd_process_defect
        FOREIGN KEY (process_defect_id) REFERENCES process_defects(id) ON DELETE SET NULL;

-- Bước 1.5: Tạo Index mới
CREATE INDEX idx_process_defect ON training_topic_detail(process_defect_id);


-- =========================================================================
-- 2. XỬ LÝ BẢNG: TRAINING_TOPIC_DETAIL_HISTORY
-- =========================================================================

-- Bảng History thường không có Foreign Key ràng buộc chặt (để giữ lịch sử khi master data bị xóa),
-- nên ta chỉ cần đổi tên cột.

ALTER TABLE training_topic_detail_history
    CHANGE COLUMN issue_detail_id process_defect_id BIGINT;


SET FOREIGN_KEY_CHECKS = 1;