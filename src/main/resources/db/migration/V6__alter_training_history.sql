
-- 1. Xóa bảng cũ (để tránh lỗi khóa ngoại)
DROP TABLE IF EXISTS training_plan_detail_history;
DROP TABLE IF EXISTS training_plan_history;
DROP TABLE IF EXISTS training_result_detail_history;
DROP TABLE IF EXISTS training_result_history;

-- 2. Tạo lại bảng HEADER: training_plan_history
CREATE TABLE training_plan_history (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Link về Plan gốc (Vẫn giữ FK này để truy vết)
                                       training_plan_id BIGINT NOT NULL,

    -- Các trường thông tin cơ bản
                                       title VARCHAR(255),
                                       version INT NOT NULL,
                                       month_start DATE,
                                       month_end DATE,

    -- SNAPSHOT: Group
                                       group_id BIGINT,
                                       group_name VARCHAR(255),

    -- SNAPSHOT: Verified By SV
                                       verified_by_sv BIGINT,
                                       verified_by_sv_name VARCHAR(255),

                                       reject_by BIGINT,
                                       reject_by_name VARCHAR(255),

    -- Thông tin từ chối
                                       reject_level ENUM('SUPERVISOR', 'MANAGER'),
                                       reject_reason TEXT,
                                       recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Các cột của BaseEntity (Bắt buộc phải có)
                                       delete_flag BOOLEAN NOT NULL DEFAULT FALSE,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       created_by NVARCHAR(255),
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       updated_by NVARCHAR(255),
    -- Tạo khóa ngoại về bảng training_plan
                                       CONSTRAINT fk_history_plan_v2 FOREIGN KEY (training_plan_id) REFERENCES training_plan(id)
);

-- 3. Tạo lại bảng DETAIL: training_plan_detail_history
CREATE TABLE training_plan_detail_history (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Link về Header History
                                              training_plan_history_id BIGINT NOT NULL,

    -- SNAPSHOT: Employee
                                              employee_id BIGINT,
                                              employee_code VARCHAR(50),
                                              employee_full_name VARCHAR(255),

    -- SNAPSHOT: Process
                                              process_id BIGINT,
                                              process_code VARCHAR(50),
                                              process_name VARCHAR(255),

    -- Ngày tháng và kết quả
                                              target_month DATE,
                                              planned_date DATE,
                                              actual_date DATE,
                                              note TEXT,
                                              result_status ENUM('PENDING', 'DONE', 'SICK_LEAVE', 'ANNUAL_LEAVE', 'NOT_APPLICABLE', 'MISSED'),

    -- Các cột của BaseEntity
                                              delete_flag BOOLEAN NOT NULL DEFAULT FALSE,
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              created_by NVARCHAR(255),
                                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                              updated_by NVARCHAR(255),

    -- Tạo khóa ngoại về bảng history header
                                              CONSTRAINT fk_detail_history_header_v2
                                                  FOREIGN KEY (training_plan_history_id)
                                                      REFERENCES training_plan_history(id)
                                                      ON DELETE CASCADE
);

-- 2. Tạo bảng HEADER: training_result_history
CREATE TABLE training_result_history (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Link về Result gốc
                                         training_result_id BIGINT NOT NULL,

    -- Thông tin cơ bản
                                         title VARCHAR(200),
                                         version INT NOT NULL,
                                         year INT,

    -- Snapshot: Group (Lưu ID và Name)
                                         group_id BIGINT,
                                         group_name VARCHAR(255),

    -- Snapshot: Verified By SV
                                         verified_by_sv BIGINT,
                                         verified_by_sv_name VARCHAR(255),

    -- Snapshot: Rejected By
                                         reject_by BIGINT,
                                         reject_by_name VARCHAR(255),

    -- Thông tin từ chối
                                         reject_level ENUM('FINAL_INSPECTION', 'SUPERVISOR', 'MANAGER'),
                                         reject_reason TEXT,

    -- BaseEntity (Bắt buộc phải có vì extends BaseEntity)
                                         delete_flag BOOLEAN NOT NULL DEFAULT FALSE,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         created_by NVARCHAR(255),
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         updated_by NVARCHAR(255),

    -- Khóa ngoại trỏ về bảng training_result gốc
                                         CONSTRAINT fk_result_history_origin FOREIGN KEY (training_result_id) REFERENCES training_result(id)
);

-- 3. Tạo bảng DETAIL: training_result_detail_history
CREATE TABLE training_result_detail_history (
                                                id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Link về bảng Detail gốc
                                                training_result_detail_id BIGINT NOT NULL,

    -- Link về History Header
                                                training_result_history_id BIGINT NOT NULL,

                                                version INT NOT NULL,
                                                actual_date DATE,

    -- Product Group
                                                product_group_id BIGINT,

    -- Thời gian
                                                time_in TIME,
                                                time_out TIME,

    -- Snapshot: Chữ ký In
                                                signature_pro_in BIGINT,
                                                signature_pro_in_name VARCHAR(255),
                                                signature_fi_in BIGINT,
                                                signature_fi_in_name VARCHAR(255),

    -- Snapshot: Chữ ký Out
                                                signature_pro_out BIGINT,
                                                signature_pro_out_name VARCHAR(255),
                                                signature_fi_out BIGINT,
                                                signature_fi_out_name VARCHAR(255),

    -- Kết quả
                                                detection_time INT,
                                                is_pass BOOLEAN,
                                                remedial_action TEXT,

    -- Snapshot: Rejected By (Ở detail)
                                                reject_by BIGINT,
                                                reject_by_name VARCHAR(255),
                                                reject_reason TEXT,

    -- BaseEntity

                                                delete_flag BOOLEAN NOT NULL DEFAULT FALSE,
                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                created_by NVARCHAR(255),
                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                updated_by NVARCHAR(255),

    -- Các Khóa ngoại
                                                FOREIGN KEY (training_result_detail_id) REFERENCES training_result_detail(id) ON DELETE CASCADE,
                                                FOREIGN KEY (training_result_history_id) REFERENCES training_result_history(id) ON DELETE CASCADE
);