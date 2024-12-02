DROP DATABASE IF EXISTS buscompany;
CREATE DATABASE buscompany;
USE buscompany;

CREATE TABLE `user` (
	`id` BIGINT AUTO_INCREMENT,
	`first_name` VARCHAR(100) NOT NULL,
	`last_name` VARCHAR(100) NOT NULL,
    `patronymic` VARCHAR(300),
    `user_type` ENUM ("ADMIN", "CLIENT") NOT NULL,
	`login` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `active_user` BOOLEAN DEFAULT TRUE,

    PRIMARY KEY (`id`),
    INDEX `first_name_index`(`first_name`),
    INDEX `last_name_index`(`last_name`),
    INDEX `patronymic_index`(`patronymic`),
    INDEX `user_type_index`(`user_type`),
    INDEX `login_index`(`login`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `administrator` (
	`user_id` BIGINT,
    `position` VARCHAR(300) NOT NULL,

	PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,

    INDEX `position_index` (`position`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `client` (
	`user_id` BIGINT,
    `email` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(100),

	PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,

    INDEX `email_index` (`email`),
    INDEX `phone_index` (`phone`)
) ENGINE = InnoDB DEFAULT CHARSET =  utf8mb4
;

CREATE TABLE `active_session` (
	`user_id` BIGINT NOT NULL,
    `java_session_id` VARCHAR(300) NOT NULL UNIQUE,
    `creation_time` DATETIME NOT NULL,

	FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,

    INDEX `user_id_index`( `user_id`),
    INDEX `java_session_id_index`( `java_session_id`),
	INDEX `creation_time_index`( `creation_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `bus` (
	`id` BIGINT AUTO_INCREMENT,
    `brand_name` VARCHAR(50) UNIQUE NOT NULL,
    `place_count` INT NOT NULL,

    PRIMARY KEY(`id`),
    INDEX `bus_brand_name_index`(`brand_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `station` (
	`id` BIGINT AUTO_INCREMENT,
    `name` VARCHAR(50) UNIQUE NOT NULL,

    PRIMARY KEY(`id`),
    INDEX `station_name_index`(`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `trip` (
	`id` BIGINT AUTO_INCREMENT,
    `bus_id` BIGINT NOT NULL,
    `from_station_id` BIGINT NOT NULL,
    `to_station_id` BIGINT NOT NULL,
    `start` TIME NOT NULL,
    `duration` TIME NOT NULL,
    `price` DECIMAL(10, 2) NOT NULL,
    `approved` BOOL DEFAULT FALSE,

    PRIMARY KEY(`id`),

    FOREIGN KEY(`bus_id`) REFERENCES `bus`(`id`),
    FOREIGN KEY(`from_station_id`) REFERENCES `station`(`id`),
    FOREIGN KEY(`to_station_id`) REFERENCES `station`(`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `trip_schedule` (
	`trip_id` BIGINT,
    `from_date` DATE NOT NULL,
    `to_date` DATE NOT NULL,
    `period` VARCHAR(100) NOT NULL,

    PRIMARY KEY(`trip_id`),
    FOREIGN KEY (`trip_id`) REFERENCES `trip`(`id`) ON DELETE CASCADE,

    INDEX `trip_id_index`(`trip_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `trip_date` (
	`id` BIGINT AUTO_INCREMENT,
    `trip_id` BIGINT,
    `date` DATE NOT NULL,

    CONSTRAINT `tripId_date` UNIQUE (`trip_id`, `date`),

    PRIMARY KEY(`id`),
    FOREIGN KEY (`trip_id`) REFERENCES `trip`(`id`) ON DELETE CASCADE,

    INDEX `trip_date_index`(`date`),
    INDEX `trip_id_index`(`trip_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `trip_date_free_seats_counter` (
    `trip_date_id` BIGINT,
	`free_seats_counter` INT NOT NULL,

    PRIMARY KEY( `trip_date_id`),
    FOREIGN KEY (`trip_date_id`) REFERENCES `trip_date`(`id`) ON DELETE CASCADE,

    INDEX `free_seats_counter_index`(`free_seats_counter`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `order` (
	`id` BIGINT AUTO_INCREMENT,
    `trip_date_id` BIGINT,
    `total_price` DECIMAL(10, 2) NOT NULL,
    `user_id` BIGINT,

    PRIMARY KEY(`id`),
    FOREIGN KEY (`trip_date_id`) REFERENCES `trip_date`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,

    INDEX `order_trip_date_id_index`(`trip_date_id`),
    INDEX `order_total_price_index`(`total_price`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `passenger` (
	`id` BIGINT AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
	`first_name` VARCHAR(100) NOT NULL,
	`last_name` VARCHAR(100) NOT NULL,
    `passport` VARCHAR(50) NOT NULL,

    PRIMARY KEY(`id`),
    FOREIGN KEY (`order_id`) REFERENCES `order`(`id`) ON DELETE CASCADE,

    INDEX `passenger_first_name_index`(`first_name`),
    INDEX `passenger_last_name_index`(`last_name`),
    INDEX `passenger_passport_index`(`passport`),
    INDEX `passenger_order_id_index`(`order_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

CREATE TABLE `passenger_seats_in_bus` (
  `id` BIGINT AUTO_INCREMENT,
  `trip_date_id` BIGINT NOT NULL,
  `passenger_id` BIGINT DEFAULT NULL,
  `passenger_seat_position_number` INT NOT NULL,

  CONSTRAINT `tripDateId_seatNumber` UNIQUE (`trip_date_id`, `passenger_seat_position_number`),

  PRIMARY KEY (`id`),
  FOREIGN KEY (`trip_date_id`) REFERENCES `trip_date`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`passenger_id`) REFERENCES `passenger`(`id`) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;
