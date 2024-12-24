CREATE TABLE `accounts`
(
    `aid` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(15),
    `status` VARCHAR(50),
    `created_at` TIMESTAMP,
    `updated_at` TIMESTAMP,
    PRIMARY KEY (`aid`)
);

CREATE TABLE `otps`
(
    `email` VARCHAR(255) NOT NULL,
    `otp` VARCHAR(10) NOT NULL,
    `expired_time` BIGINT NOT NULL,
    `is_active` BOOLEAN NOT NULL,
    PRIMARY KEY (`email`)
);