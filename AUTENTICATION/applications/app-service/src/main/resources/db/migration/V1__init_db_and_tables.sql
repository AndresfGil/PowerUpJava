-- Create target database if not exists (uses Flyway placeholder ${db})
CREATE DATABASE IF NOT EXISTS `${db}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create roles table
CREATE TABLE IF NOT EXISTS `${db}`.`roles` (
    `rol_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(255) NULL,
    PRIMARY KEY (`rol_id`)
    ) ENGINE=InnoDB;

-- Insert default roles (only if table is empty)
INSERT INTO `${db}`.`roles` (`name`, `description`)
SELECT * FROM (SELECT 'ADMIN', 'Administrador del sistema') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`roles` WHERE `name` = 'ADMIN');

INSERT INTO `${db}`.`roles` (`name`, `description`)
SELECT * FROM (SELECT 'USER', 'Usuario estándar') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `${db}`.`roles` WHERE `name` = 'USER');

-- Create usuarios table
CREATE TABLE IF NOT EXISTS `${db}`.`users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `lastname` VARCHAR(100) NOT NULL,
    `birth_date` VARCHAR(20) NULL,
    `address` VARCHAR(200) NULL,
    `phone` VARCHAR(50) NULL,
    `email` VARCHAR(150) NULL,
    `base_salary` DECIMAL(15,2) NULL,
    `document_id` VARCHAR(50) NULL,
    `rol_id` BIGINT NULL,
    PRIMARY KEY (`user_id`),
    CONSTRAINT `fk_users_roles` FOREIGN KEY (`rol_id`) REFERENCES `${db}`.`roles`(`rol_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
    ) ENGINE=InnoDB;

-- Helpful index for FK
CREATE INDEX `idx_users_rol_id` ON `${db}`.`users` (`rol_id`);
