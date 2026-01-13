-- Flyway migration V1: initialize schema for anomaly_training
-- This migration creates all application tables. It does NOT create the database itself.

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100),
  `role` ENUM('ADMIN','MANAGER','SUPERVISOR','TEAM_LEADER_PRO','TEAM_LEADER_FI') NOT NULL,
  `is_active` BOOLEAN DEFAULT TRUE,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `sections` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `code` VARCHAR(50) UNIQUE,
  `manager_id` BIGINT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_sections_manager` FOREIGN KEY (`manager_id`) REFERENCES `users`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `groups` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `section_id` BIGINT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `code` VARCHAR(50),
  `supervisor_id` BIGINT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_groups_section` FOREIGN KEY (`section_id`) REFERENCES `sections`(`id`),
  CONSTRAINT `fk_groups_supervisor` FOREIGN KEY (`supervisor_id`) REFERENCES `users`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `teams` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `group_id` BIGINT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `team_leader_id` BIGINT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_teams_group` FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`),
  CONSTRAINT `fk_teams_leader` FOREIGN KEY (`team_leader_id`) REFERENCES `users`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `processes` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `group_id` BIGINT NOT NULL,
  `code` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `classification` ENUM('1','2','3','4') DEFAULT '4',
  `standard_time_jt` INT COMMENT 'Thời gian tính bằng giây',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_processes_group` FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `group_products` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `group_id` BIGINT NOT NULL,
  `product_code` VARCHAR(50) NOT NULL,
  CONSTRAINT `fk_pp_process` FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`),
  UNIQUE KEY `unique_group_prod` (`group_id`, `product_code`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `employees` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `employee_code` VARCHAR(20) NOT NULL UNIQUE,
  `full_name` VARCHAR(100) NOT NULL,
  `team_id` BIGINT,
  `status` ENUM('ACTIVE','MATERNITY_LEAVE','RESIGNED') DEFAULT 'ACTIVE',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_employees_team` FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `employee_skills` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `employee_id` BIGINT NOT NULL,
  `process_id` BIGINT NOT NULL,
  `certification_date` DATE,
  `is_qualified` BOOLEAN DEFAULT TRUE,
  CONSTRAINT `fk_emsk_employee` FOREIGN KEY (`employee_id`) REFERENCES `employees`(`id`),
  CONSTRAINT `fk_emsk_process` FOREIGN KEY (`process_id`) REFERENCES `processes`(`id`),
  UNIQUE KEY `unique_skill` (`employee_id`, `process_id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `past_defects` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `process_id` BIGINT NOT NULL,
  `defect_description` TEXT NOT NULL,
  `detected_date` DATE,
  `root_cause` TEXT,
  `countermeasure` TEXT,
  `is_escaped` BOOLEAN DEFAULT FALSE,
  `created_by` BIGINT,
  `status` ENUM('DRAFT','APPROVED','REJECTED') DEFAULT 'DRAFT',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_pd_process` FOREIGN KEY (`process_id`) REFERENCES `processes`(`id`),
  CONSTRAINT `fk_pd_creator` FOREIGN KEY (`created_by`) REFERENCES `users`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `training_samples` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `code` VARCHAR(50) UNIQUE,
  `name` VARCHAR(200) NOT NULL,
  `past_defect_id` BIGINT UNIQUE,
  `process_id` BIGINT NOT NULL,
  `description` TEXT,
  `storage_location` VARCHAR(100),
  `is_active` BOOLEAN DEFAULT TRUE,
  `created_by` BIGINT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_ts_defect` FOREIGN KEY (`past_defect_id`) REFERENCES `past_defects`(`id`),
  CONSTRAINT `fk_ts_process` FOREIGN KEY (`process_id`) REFERENCES `processes`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `training_plans` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `group_id` BIGINT NOT NULL,
  `name` VARCHAR(200),
  `start_date` DATE,
  `end_date` DATE,
  `created_by` BIGINT,
  `approved_by_sv` BIGINT,
  `approved_at_sv` TIMESTAMP NULL,
  `approved_by_mgr` BIGINT,
  `approved_at_mgr` TIMESTAMP NULL,
  `status` ENUM('DRAFT','PENDING_SV','PENDING_MGR','APPROVED','REJECTED') DEFAULT 'DRAFT',
  `note` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_plan_group` FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`),
  CONSTRAINT `fk_plan_creator` FOREIGN KEY (`created_by`) REFERENCES `users`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `training_plan_details` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `plan_id` BIGINT NOT NULL,
  `employee_id` BIGINT NOT NULL,
  `training_sample_id` BIGINT NOT NULL,
  `planned_date` DATE,
  `status` ENUM('PLANNED','COMPLETED','SKIPPED') DEFAULT 'PLANNED',
  `note` TEXT,
  CONSTRAINT `fk_tpd_plan` FOREIGN KEY (`plan_id`) REFERENCES `training_plans`(`id`),
  CONSTRAINT `fk_tpd_employee` FOREIGN KEY (`employee_id`) REFERENCES `employees`(`id`),
  CONSTRAINT `fk_tpd_sample` FOREIGN KEY (`training_sample_id`) REFERENCES `training_samples`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `training_results` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `plan_detail_id` BIGINT NOT NULL,
  `actual_date` DATE NOT NULL,
  `group_product_id` BIGINT,
  `time_in` TIME,
  `time_out` TIME,
  `detection_time` INT,
  `is_pass` BOOLEAN NOT NULL,
  `remedial_action` TEXT,
  `signature_pro_id` BIGINT,
  `signature_pro_at` TIMESTAMP NULL,
  `signature_fi_id` BIGINT,
  `signature_fi_at` TIMESTAMP NULL,
  `verified_by_sv` BIGINT,
  `verified_at_sv` TIMESTAMP NULL,
  `approved_by_mgr` BIGINT,
  `approved_at_mgr` TIMESTAMP NULL,
  `status` ENUM('DRAFT','SUBMITTED','VERIFIED_SV','APPROVED_MGR','REJECTED') DEFAULT 'DRAFT',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_tr_plandetail` FOREIGN KEY (`plan_detail_id`) REFERENCES `training_plan_details`(`id`),
  CONSTRAINT `fk_tr_process_product` FOREIGN KEY (`group_product_id`) REFERENCES `group_products`(`id`),
  CONSTRAINT `fk_tr_sig_pro` FOREIGN KEY (`signature_pro_id`) REFERENCES `users`(`id`),
  CONSTRAINT `fk_tr_sig_fi` FOREIGN KEY (`signature_fi_id`) REFERENCES `users`(`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS=1;

