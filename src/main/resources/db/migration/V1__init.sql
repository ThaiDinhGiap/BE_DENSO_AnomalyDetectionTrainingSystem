-- Flyway migration V1: initialize schema for anomaly_training
-- This migration creates all application tables. It does NOT create the database itself.


-- CREATE DATABASE IF NOT EXISTS anomaly_training
--     CHARACTER SET utf8mb4
--     COLLATE utf8mb4_unicode_ci;

-- Flyway migration V1: Initialize schema for anomaly_training
SET FOREIGN_KEY_CHECKS=0;

-- Drop tables if exists (Reverse order of creation)
DROP TABLE IF EXISTS training_result_detail_history;
DROP TABLE IF EXISTS training_result_detail;
DROP TABLE IF EXISTS training_result_history;
DROP TABLE IF EXISTS training_result;
DROP TABLE IF EXISTS training_plan_detail_history;
DROP TABLE IF EXISTS training_plan_detail;
DROP TABLE IF EXISTS training_plan_history;
DROP TABLE IF EXISTS training_plan;
DROP TABLE IF EXISTS defect_training_content;
DROP TABLE IF EXISTS training_topic_detail_history;
DROP TABLE IF EXISTS training_topic_detail;
DROP TABLE IF EXISTS training_topics_history;
DROP TABLE IF EXISTS training_topics;
DROP TABLE IF EXISTS process_defects;
DROP TABLE IF EXISTS issue_detail_history;
DROP TABLE IF EXISTS issue_detail;
DROP TABLE IF EXISTS issue_report_history;
DROP TABLE IF EXISTS issue_report;
DROP TABLE IF EXISTS process_qualifications;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS product_group;
DROP TABLE IF EXISTS processes;
DROP TABLE IF EXISTS `groups`;
DROP TABLE IF EXISTS sections;
DROP TABLE IF EXISTS notification_queue;
DROP TABLE IF EXISTS notification_settings;
DROP TABLE IF EXISTS notification_templates;
DROP TABLE IF EXISTS users;

-- ============================================
-- 1. USERS TABLE
-- ============================================
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       role ENUM('MANAGER', 'SUPERVISOR', 'TEAM_LEADER', 'FINAL_INSPECTION') NOT NULL,
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_role (role),
                       INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. NOTIFICATION_TEMPLATES TABLE
-- ============================================
CREATE TABLE notification_templates (
                                        code VARCHAR(50) PRIMARY KEY,
                                        subject_template VARCHAR(200) NOT NULL,
                                        body_template TEXT NOT NULL,
                                        description VARCHAR(500),
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 3. NOTIFICATION_SETTINGS TABLE
-- ============================================
CREATE TABLE notification_settings (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       template_code VARCHAR(50) NOT NULL,
                                       is_enabled BOOLEAN DEFAULT TRUE,
                                       remind_before_days INT DEFAULT 3,
                                       is_persistent BOOLEAN DEFAULT FALSE,
                                       remind_interval_hours INT DEFAULT 24,
                                       max_reminders INT DEFAULT 5,
                                       preferred_send_time TIME DEFAULT '08:00:00',
                                       escalate_after_days INT,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (template_code) REFERENCES notification_templates(code) ON DELETE CASCADE,
                                       INDEX idx_template (template_code),
                                       INDEX idx_enabled (is_enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 4. NOTIFICATION_QUEUE TABLE
-- ============================================
CREATE TABLE notification_queue (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    recipient_user_id BIGINT NOT NULL,
                                    cc_list TEXT,
                                    notification_type VARCHAR(50) NOT NULL,
                                    related_entity_id BIGINT,
                                    related_entity_table VARCHAR(50),
                                    subject VARCHAR(255) NOT NULL,
                                    body TEXT NOT NULL,
                                    status ENUM('PENDING','SENDING','SENT','FAILED') DEFAULT 'PENDING',
                                    retry_count INT DEFAULT 0,
                                    max_retries INT DEFAULT 3,
                                    error_message TEXT,
                                    scheduled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    sent_at TIMESTAMP NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_nq_user FOREIGN KEY (recipient_user_id) REFERENCES users(id),
                                    CONSTRAINT fk_nq_template FOREIGN KEY (notification_type) REFERENCES notification_templates(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 5. SECTIONS TABLE
-- ============================================
CREATE TABLE sections (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          manager_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE RESTRICT,
                          INDEX idx_manager (manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 6. GROUPS TABLE
-- ============================================
CREATE TABLE `groups` (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        section_id BIGINT NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        supervisor_id BIGINT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE RESTRICT,
                        FOREIGN KEY (supervisor_id) REFERENCES users(id) ON DELETE RESTRICT,
                        INDEX idx_section (section_id),
                        INDEX idx_supervisor (supervisor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 7. PROCESSES TABLE
-- ============================================
CREATE TABLE processes (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           group_id BIGINT NOT NULL,
                           code VARCHAR(20) NOT NULL,
                           name VARCHAR(200) NOT NULL,
                           description TEXT,
                           classification TINYINT NOT NULL DEFAULT 1,
                           standard_time_jt DECIMAL(10,2),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE RESTRICT,
                           UNIQUE KEY uk_group_code (group_id, code),
                           INDEX idx_classification (classification)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 8. PRODUCT_GROUP TABLE
-- ============================================
CREATE TABLE product_group (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               group_id BIGINT NOT NULL,
                               product_code VARCHAR(50) NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE RESTRICT,
                               UNIQUE KEY uk_group_product (group_id, product_code),
                               INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 9. TEAMS TABLE
-- ============================================
CREATE TABLE teams (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       group_id BIGINT NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       team_leader_id BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE RESTRICT,
                       FOREIGN KEY (team_leader_id) REFERENCES users(id) ON DELETE RESTRICT,
                       INDEX idx_group (group_id),
                       INDEX idx_team_leader (team_leader_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 10. EMPLOYEES TABLE
-- ============================================
CREATE TABLE employees (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           employee_code VARCHAR(20) NOT NULL UNIQUE,
                           full_name VARCHAR(100) NOT NULL,
                           team_id BIGINT NOT NULL,
                           status ENUM('ACTIVE', 'MATERNITY_LEAVE', 'RESIGNED') DEFAULT 'ACTIVE',
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE RESTRICT,
                           INDEX idx_team (team_id),
                           INDEX idx_status (status),
                           INDEX idx_employee_code (employee_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 11. PROCESS_QUALIFICATIONS TABLE
-- ============================================
CREATE TABLE process_qualifications (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        employee_id BIGINT NOT NULL,
                                        process_id BIGINT NOT NULL,
                                        is_qualified BOOLEAN DEFAULT TRUE,
                                        certified_date DATE,
                                        expiry_date DATE,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
                                        FOREIGN KEY (process_id) REFERENCES processes(id) ON DELETE CASCADE,
                                        UNIQUE KEY uk_employee_process (employee_id, process_id),
                                        INDEX idx_qualified (is_qualified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 12. ISSUE_REPORT TABLE
-- ============================================
CREATE TABLE issue_report (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              created_by_tl BIGINT NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              verified_by_sv BIGINT,
                              verified_at_sv TIMESTAMP NULL,
                              approved_by_manager BIGINT,
                              approved_at_manager TIMESTAMP NULL,
                              status ENUM('DRAFT', 'WAITING_SV', 'REJECTED_BY_SV', 'WAITING_MANAGER', 'REJECTED_BY_MANAGER', 'APPROVED', 'TRAINING_REQUIRED', 'TRAINING_CREATED', 'NEED_UPDATE') DEFAULT 'DRAFT',
                              rejected_by BIGINT,
                              reject_level ENUM('SUPERVISOR', 'MANAGER'),
                              reject_reason TEXT,
                              current_version INT DEFAULT 1,
                              last_reject_reason TEXT,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (created_by_tl) REFERENCES users(id) ON DELETE RESTRICT,
                              FOREIGN KEY (verified_by_sv) REFERENCES users(id) ON DELETE RESTRICT,
                              FOREIGN KEY (approved_by_manager) REFERENCES users(id) ON DELETE RESTRICT,
                              FOREIGN KEY (rejected_by) REFERENCES users(id) ON DELETE RESTRICT,
                              INDEX idx_status (status),
                              INDEX idx_created_by (created_by_tl),
                              INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 13. ISSUE_REPORT_HISTORY TABLE
-- ============================================
CREATE TABLE issue_report_history (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      issue_report_id BIGINT NOT NULL,
                                      version INT NOT NULL,
                                      rejected_by BIGINT,
                                      reject_reason TEXT,
                                      recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (issue_report_id) REFERENCES issue_report(id) ON DELETE CASCADE,
                                      FOREIGN KEY (rejected_by) REFERENCES users(id) ON DELETE SET NULL,
                                      INDEX idx_issue_report (issue_report_id),
                                      INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 14. ISSUE_DETAIL TABLE
-- ============================================
CREATE TABLE issue_detail (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              issue_report_id BIGINT NOT NULL,
                              defect_description TEXT NOT NULL,
                              process_id BIGINT NOT NULL,
                              detected_date DATE NOT NULL,
                              is_escaped BOOLEAN DEFAULT FALSE,
                              note TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (issue_report_id) REFERENCES issue_report(id) ON DELETE CASCADE,
                              FOREIGN KEY (process_id) REFERENCES processes(id) ON DELETE RESTRICT,
                              INDEX idx_issue_report (issue_report_id),
                              INDEX idx_process (process_id),
                              INDEX idx_detected_date (detected_date),
                              INDEX idx_is_escaped (is_escaped)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 15. ISSUE_DETAIL_HISTORY TABLE
-- ============================================
CREATE TABLE issue_detail_history (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      issue_report_history_id BIGINT NOT NULL,
                                      defect_description TEXT NOT NULL,
                                      process_id BIGINT NOT NULL,
                                      detected_date DATE NOT NULL,
                                      is_escaped BOOLEAN DEFAULT FALSE,
                                      note TEXT,
                                      FOREIGN KEY (issue_report_history_id) REFERENCES issue_report_history(id) ON DELETE CASCADE,
                                      INDEX idx_history (issue_report_history_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 16. PROCESS_DEFECTS TABLE
-- ============================================
CREATE TABLE process_defects (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 issue_detail_id BIGINT NOT NULL,
                                 defect_description TEXT NOT NULL,
                                 process_id BIGINT NOT NULL,
                                 detected_date DATE NOT NULL,
                                 is_escaped BOOLEAN DEFAULT FALSE,
                                 note TEXT,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (issue_detail_id) REFERENCES issue_detail(id) ON DELETE CASCADE,
                                 FOREIGN KEY (process_id) REFERENCES processes(id) ON DELETE RESTRICT,
                                 UNIQUE KEY uk_issue_detail (issue_detail_id),
                                 INDEX idx_process (process_id),
                                 INDEX idx_detected_date (detected_date),
                                 INDEX idx_is_escaped (is_escaped)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 17. TRAINING_TOPICS TABLE
-- ============================================
CREATE TABLE training_topics (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 version INT DEFAULT 1,
                                 title VARCHAR(200) NOT NULL,
                                 created_by_tl BIGINT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 verified_by_sv BIGINT,
                                 verified_at_sv TIMESTAMP NULL,
                                 approved_by_manager BIGINT,
                                 approved_at_manager TIMESTAMP NULL,
                                 status ENUM('DRAFT', 'WAITING_SV', 'REJECTED_BY_SV', 'WAITING_MANAGER', 'REJECTED_BY_MANAGER', 'APPROVED', 'NEED_UPDATE') DEFAULT 'DRAFT',
                                 current_version INT DEFAULT 1,
                                 last_reject_reason TEXT,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (created_by_tl) REFERENCES users(id) ON DELETE RESTRICT,
                                 FOREIGN KEY (verified_by_sv) REFERENCES users(id) ON DELETE RESTRICT,
                                 FOREIGN KEY (approved_by_manager) REFERENCES users(id) ON DELETE RESTRICT,
                                 INDEX idx_status (status),
                                 INDEX idx_created_by (created_by_tl)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 18. TRAINING_TOPICS_HISTORY TABLE
-- ============================================
CREATE TABLE training_topics_history (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         training_topic_id BIGINT NOT NULL,
                                         version INT NOT NULL,
                                         created_by_tl BIGINT,
                                         created_at TIMESTAMP NULL,
                                         verified_by_sv BIGINT,
                                         verified_at_sv TIMESTAMP NULL,
                                         approved_by_manager BIGINT,
                                         approved_at_manager TIMESTAMP NULL,
                                         status ENUM('DRAFT', 'WAITING_SV', 'REJECTED_BY_SV', 'WAITING_MANAGER', 'REJECTED_BY_MANAGER', 'APPROVED', 'NEED_UPDATE'),
                                         recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (training_topic_id) REFERENCES training_topics(id) ON DELETE CASCADE,
                                         INDEX idx_topic (training_topic_id),
                                         INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 19. TRAINING_TOPIC_DETAIL TABLE
-- ============================================
CREATE TABLE training_topic_detail (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       topic_id BIGINT NOT NULL,
                                       issue_detail_id BIGINT,
                                       category_name VARCHAR(200) NOT NULL,
                                       training_sample TEXT,
                                       training_detail TEXT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (topic_id) REFERENCES training_topics(id) ON DELETE CASCADE,
                                       FOREIGN KEY (issue_detail_id) REFERENCES issue_detail(id) ON DELETE SET NULL,
                                       INDEX idx_topic (topic_id),
                                       INDEX idx_issue_detail (issue_detail_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 20. TRAINING_TOPIC_DETAIL_HISTORY TABLE
-- ============================================
CREATE TABLE training_topic_detail_history (
                                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                               topic_history_id BIGINT NOT NULL,
                                               issue_detail_id BIGINT,
                                               category_name VARCHAR(200) NOT NULL,
                                               training_sample TEXT,
                                               training_detail TEXT NOT NULL,
                                               FOREIGN KEY (topic_history_id) REFERENCES training_topics_history(id) ON DELETE CASCADE,
                                               INDEX idx_topic_history (topic_history_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 21. DEFECT_TRAINING_CONTENT TABLE
-- ============================================
CREATE TABLE defect_training_content (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         training_topic_detail_id BIGINT NOT NULL,
                                         process_defect_id BIGINT NOT NULL,
                                         category_name VARCHAR(200) NOT NULL,
                                         training_sample TEXT,
                                         training_detail TEXT NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         FOREIGN KEY (training_topic_detail_id) REFERENCES training_topic_detail(id) ON DELETE CASCADE,
                                         FOREIGN KEY (process_defect_id) REFERENCES process_defects(id) ON DELETE RESTRICT,
                                         INDEX idx_topic_detail (training_topic_detail_id),
                                         INDEX idx_process_defect (process_defect_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 22. TRAINING_PLAN TABLE
-- ============================================
CREATE TABLE training_plan (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               title VARCHAR(200) NOT NULL,
                               month_start DATE NOT NULL,
                               month_end DATE NOT NULL,
                               group_id BIGINT NOT NULL,
                               created_by_tl BIGINT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               verified_by_sv BIGINT,
                               verified_at_sv TIMESTAMP NULL,
                               approved_by_manager BIGINT,
                               approved_at_manager TIMESTAMP NULL,
                               status ENUM('DRAFT', 'WAITING_SV', 'REJECTED_BY_SV', 'WAITING_MANAGER', 'REJECTED_BY_MANAGER', 'APPROVED') DEFAULT 'DRAFT',
                               current_version INT DEFAULT 1,
                               last_reject_reason TEXT,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE RESTRICT,
                               FOREIGN KEY (created_by_tl) REFERENCES users(id) ON DELETE RESTRICT,
                               FOREIGN KEY (verified_by_sv) REFERENCES users(id) ON DELETE RESTRICT,
                               FOREIGN KEY (approved_by_manager) REFERENCES users(id) ON DELETE RESTRICT,
                               INDEX idx_status (status),
                               INDEX idx_group (group_id),
                               INDEX idx_month_range (month_start, month_end),
                               INDEX idx_created_by (created_by_tl)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 23. TRAINING_PLAN_HISTORY TABLE
-- ============================================
CREATE TABLE training_plan_history (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       training_plan_id BIGINT NOT NULL,
                                       title VARCHAR(200),
                                       version INT NOT NULL,
                                       month_start DATE,
                                       month_end DATE,
                                       group_id BIGINT,
                                       created_by_tl BIGINT,
                                       verified_by_sv BIGINT,
                                       rejected_by BIGINT,
                                       reject_level ENUM('SUPERVISOR', 'MANAGER'),
                                       reject_reason TEXT,
                                       recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (training_plan_id) REFERENCES training_plan(id) ON DELETE CASCADE,
                                       FOREIGN KEY (rejected_by) REFERENCES users(id) ON DELETE SET NULL,
                                       INDEX idx_plan (training_plan_id),
                                       INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 24. TRAINING_PLAN_DETAIL TABLE
-- ============================================
CREATE TABLE training_plan_detail (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      training_plan_id BIGINT NOT NULL,
                                      employee_id BIGINT NOT NULL,
                                      process_id BIGINT NOT NULL,
                                      target_month DATE NOT NULL,
                                      planned_date DATE NOT NULL,
                                      actual_date DATE,
                                      note TEXT,
                                      result_status ENUM('PENDING', 'DONE', 'SICK_LEAVE', 'ANNUAL_LEAVE', 'NOT_APPLICABLE', 'MISSED') DEFAULT 'PENDING',
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      FOREIGN KEY (training_plan_id) REFERENCES training_plan(id) ON DELETE CASCADE,
                                      FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE RESTRICT,
                                      FOREIGN KEY (process_id) REFERENCES processes(id) ON DELETE RESTRICT,
                                      INDEX idx_plan (training_plan_id),
                                      INDEX idx_employee (employee_id),
                                      INDEX idx_process (process_id),
                                      INDEX idx_target_month (target_month),
                                      INDEX idx_planned_date (planned_date),
                                      INDEX idx_result_status (result_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 25. TRAINING_PLAN_DETAIL_HISTORY TABLE
-- ============================================
CREATE TABLE training_plan_detail_history (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              training_plan_history_id BIGINT NOT NULL,
                                              employee_id BIGINT,
                                              process_id BIGINT,
                                              target_month DATE,
                                              planned_date DATE,
                                              actual_date DATE,
                                              note TEXT,
                                              result_status ENUM('PENDING', 'DONE', 'SICK_LEAVE', 'ANNUAL_LEAVE', 'NOT_APPLICABLE', 'MISSED'),
                                              FOREIGN KEY (training_plan_history_id) REFERENCES training_plan_history(id) ON DELETE CASCADE,
                                              INDEX idx_plan_history (training_plan_history_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 26. TRAINING_RESULT TABLE
-- ============================================
CREATE TABLE training_result (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 title VARCHAR(200) NOT NULL,
                                 year INT NOT NULL,
                                 group_id BIGINT NOT NULL,
                                 created_by_tl BIGINT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 confirm_by_fi BIGINT,
                                 confirm_at_fi TIMESTAMP NULL,
                                 verified_by_sv BIGINT,
                                 verified_at_sv TIMESTAMP NULL,
                                 approved_by_manager BIGINT,
                                 approved_at_manager TIMESTAMP NULL,
                                 status ENUM('DRAFT', 'WAITING_FI', 'REJECTED_BY_FI', 'APPROVED_BY_FI', 'WAITING_SV', 'REJECTED_BY_SV', 'APPROVED_BY_SV', 'WAITING_MANAGER', 'REJECTED_BY_MANAGER', 'APPROVED_BY_MANAGER') DEFAULT 'DRAFT',
                                 current_version INT DEFAULT 1,
                                 last_reject_reason TEXT,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE RESTRICT,
                                 FOREIGN KEY (created_by_tl) REFERENCES users(id) ON DELETE RESTRICT,
                                 FOREIGN KEY (confirm_by_fi) REFERENCES users(id) ON DELETE RESTRICT,
                                 FOREIGN KEY (verified_by_sv) REFERENCES users(id) ON DELETE RESTRICT,
                                 FOREIGN KEY (approved_by_manager) REFERENCES users(id) ON DELETE RESTRICT,
                                 INDEX idx_status (status),
                                 INDEX idx_group (group_id),
                                 INDEX idx_year (year),
                                 INDEX idx_created_by (created_by_tl)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 27. TRAINING_RESULT_HISTORY TABLE
-- ============================================
CREATE TABLE training_result_history (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         training_result_id BIGINT NOT NULL,
                                         title VARCHAR(200),
                                         version INT NOT NULL,
                                         year INT,
                                         group_id BIGINT,
                                         created_by_tl BIGINT,
                                         verified_by_sv BIGINT,
                                         rejected_by BIGINT,
                                         reject_level ENUM('FINAL_INSPECTION', 'SUPERVISOR', 'MANAGER'),
                                         reject_reason TEXT,
                                         recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (training_result_id) REFERENCES training_result(id) ON DELETE CASCADE,
                                         FOREIGN KEY (rejected_by) REFERENCES users(id) ON DELETE SET NULL,
                                         INDEX idx_result (training_result_id),
                                         INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 28. TRAINING_RESULT_DETAIL TABLE
-- ============================================
CREATE TABLE training_result_detail (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        training_result_id BIGINT NOT NULL,
                                        training_plan_detail_id BIGINT NOT NULL,
                                        actual_date DATE NOT NULL,
                                        product_group_id BIGINT NOT NULL,
                                        training_topic_id BIGINT,
                                        time_in TIME NOT NULL,
                                        time_out TIME NOT NULL,
                                        signature_pro_in BIGINT,
                                        signature_fi_in BIGINT,
                                        signature_pro_out BIGINT,
                                        signature_fi_out BIGINT,
                                        detection_time INT,
                                        is_pass BOOLEAN,
                                        remedial_action TEXT,
                                        sv_confirmation BIGINT,
                                        status ENUM('DRAFT', 'WAITING_FI', 'REJECTED_BY_FI', 'APPROVED_BY_FI', 'WAITING_SV', 'REJECTED_BY_SV', 'APPROVED_BY_SV', 'WAITING_MANAGER', 'REJECTED_BY_MANAGER', 'APPROVED_BY_MANAGER') DEFAULT 'DRAFT',
                                        current_version INT DEFAULT 1,
                                        last_reject_reason TEXT,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        FOREIGN KEY (training_result_id) REFERENCES training_result(id) ON DELETE CASCADE,
                                        FOREIGN KEY (training_plan_detail_id) REFERENCES training_plan_detail(id) ON DELETE RESTRICT,
                                        FOREIGN KEY (product_group_id) REFERENCES product_group(id) ON DELETE RESTRICT,
                                        FOREIGN KEY (training_topic_id) REFERENCES training_topics(id) ON DELETE SET NULL,
                                        FOREIGN KEY (signature_pro_in) REFERENCES users(id) ON DELETE SET NULL,
                                        FOREIGN KEY (signature_fi_in) REFERENCES users(id) ON DELETE SET NULL,
                                        FOREIGN KEY (signature_pro_out) REFERENCES users(id) ON DELETE SET NULL,
                                        FOREIGN KEY (signature_fi_out) REFERENCES users(id) ON DELETE SET NULL,
                                        FOREIGN KEY (sv_confirmation) REFERENCES users(id) ON DELETE SET NULL,
                                        INDEX idx_result (training_result_id),
                                        INDEX idx_plan_detail (training_plan_detail_id),
                                        INDEX idx_actual_date (actual_date),
                                        INDEX idx_status (status),
                                        INDEX idx_is_pass (is_pass)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 29. TRAINING_RESULT_DETAIL_HISTORY TABLE
-- ============================================
CREATE TABLE training_result_detail_history (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                training_result_detail_id BIGINT NOT NULL,
                                                training_result_history_id BIGINT NOT NULL,
                                                version INT NOT NULL,
                                                actual_date DATE,
                                                product_group_id BIGINT,
                                                time_in TIME,
                                                time_out TIME,
                                                signature_pro_in BIGINT,
                                                signature_fi_in BIGINT,
                                                signature_pro_out BIGINT,
                                                signature_fi_out BIGINT,
                                                detection_time INT,
                                                is_pass BOOLEAN,
                                                remedial_action TEXT,
                                                rejected_by BIGINT,
                                                reject_reason TEXT,
                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                FOREIGN KEY (training_result_detail_id) REFERENCES training_result_detail(id) ON DELETE CASCADE,
                                                FOREIGN KEY (training_result_history_id) REFERENCES training_result_history(id) ON DELETE CASCADE,
                                                FOREIGN KEY (rejected_by) REFERENCES users(id) ON DELETE SET NULL,
                                                INDEX idx_detail (training_result_detail_id),
                                                INDEX idx_result_history (training_result_history_id),
                                                INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS=1;