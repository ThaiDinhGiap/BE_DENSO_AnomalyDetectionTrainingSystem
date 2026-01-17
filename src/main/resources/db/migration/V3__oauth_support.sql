-- 1. Cho phép password NULL cho OAuth users
ALTER TABLE users MODIFY password_hash VARCHAR(255) NULL;

-- 2. Thêm OAuth fields
ALTER TABLE users ADD COLUMN oauth_provider ENUM('LOCAL', 'MICROSOFT') DEFAULT 'LOCAL' AFTER role;
ALTER TABLE users ADD COLUMN oauth_provider_id VARCHAR(255) AFTER oauth_provider;

-- 3. Index cho OAuth lookup
CREATE INDEX idx_oauth_provider_id ON users(oauth_provider, oauth_provider_id);

-- 4. Bảng refresh tokens
CREATE TABLE refresh_tokens (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            user_id BIGINT NOT NULL,
            token VARCHAR(500) NOT NULL,
            expires_at TIMESTAMP NOT NULL,
            revoked BOOLEAN DEFAULT FALSE,
            device_info VARCHAR(255),
            ip_address VARCHAR(45),

            delete_flag BOOLEAN NOT NULL DEFAULT FALSE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            created_by NVARCHAR(255),
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            updated_by NVARCHAR(255),

            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
            INDEX idx_token (token(191)),
            INDEX idx_user_active (user_id, revoked, expires_at),
            INDEX idx_delete_flag (delete_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;