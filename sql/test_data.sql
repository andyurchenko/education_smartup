USE buscompany;
INSERT INTO `user` (`first_name`, `last_name`, `patronymic`, `user_type`, `login`, `password`)
VALUES ("admin_first_name_1", "admin_last_name_1", "admin_patronymic_1", "ADMIN", "admin_login_1", "admin_password_1");
INSERT INTO `administrator` (`user_id`, `position`) VALUES (LAST_INSERT_ID(), "POSITION_A");

INSERT INTO `user` (`first_name`, `last_name`, `patronymic`, `user_type`, `login`, `password`)
VALUES ("client_first_name_1", "client_last_name_1", "client_patronymic_1", "CLIENT", "client_login_1", "client_password_1");

INSERT INTO `client` (`user_id`, `email`, `phone`) VALUES (LAST_INSERT_ID(), "client_email_1", "8(913) 111-11-11") ;

INSERT INTO `user` (`first_name`, `last_name`, `patronymic`, `user_type`, `login`, `password`)
VALUES ("client_first_name_2", "client_last_name_2", "client_patronymic_2", "CLIENT", "client_login_2", "client_password_2");

INSERT INTO `client` (`user_id`, `email`, `phone`) VALUES (LAST_INSERT_ID(), "client_email_2", "8(913) 222-22-22");

INSERT INTO `active_session`(`user_id`, `java_session_id`, `creation_time`)
VALUES (1, "1", "2028-12-31 19:40:00"), (2, "2", "2028-12-31 19:40:00"), (3, "3", "2028-12-31 19:40:00");

INSERT INTO `station`(`name`) VALUES ("station_ONE"), ("station_TWO");
INSERT INTO `bus`(`brand_name`, `place_count`) VALUES ("bus_brand_name_ONE", 10), ("bus_brand_name_TWO", 20);

INSERT INTO `trip`(`bus_id`, `from_station_id`, `to_station_id`, `start`, `duration`, `price`)
VALUES (
	(SELECT `id` FROM bus WHERE `brand_name` = "bus_brand_name_ONE"),
    (SELECT `id` FROM station WHERE `name` = "station_ONE"),
    (SELECT `id` FROM station WHERE `name` = "station_TWO"),
    "14:00",
    "28:15",
    42.00
);

INSERT INTO `trip_date`(`trip_id`, `date`)
VALUES (1, "2000-01-01"), (1, "2000-01-02"), (1, "2000-01-03"), (1, "2000-01-04");

INSERT INTO `trip_date_free_seats_counter`(`trip_date_id`, `free_seats_counter`)
VALUES (1, 1), (2, 5), (3, 10), (4, 20);

-- INSERT INTO `trip_schedule`(`trip_id`,  `from_date`, `to_date`, `period`)
-- VALUES (1, "2030-01-01", "2030-12-12", "even");

INSERT INTO `order`(`trip_date_id`, `total_price`, `user_id`)
VALUES (2, 100.55, 2);

INSERT INTO `passenger`(`order_id`, `first_name`, `last_name`, `passport`)
VALUES (1, "passenger_first_name_ONE", "passenger_last_name_ONE", "passport_ONE"), (1, "passenger_first_name_TWO", "passenger_last_name_TWO", "passport_TWO");

UPDATE `trip_date_free_seats_counter`
SET `free_seats_counter` = `free_seats_counter` - 2
WHERE `trip_date_id` = 2 AND `free_seats_counter` > 2;


INSERT INTO `passenger_seats_in_bus`(`trip_date_id`, `passenger_seat_position_number`)
VALUES (2, 1), (2, 2), (2, 3), (2, 4), (2, 5);




