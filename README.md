CREATE DATABASE k_mbti CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci	
USE k_mbti


CREATE TABLE users (     
id BIGINT AUTO_INCREMENT PRIMARY KEY,     
email VARCHAR(100) NOT NULL UNIQUE,     
nickname VARCHAR(50) NOT NULL,     
password VARCHAR(255) NOT NULL,     
created_at DATETIME DEFAULT CURRENT_TIMESTAMP 
);

CREATE TABLE inquiry (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  writer VARCHAR(100) NOT NULL,
  created_at DATETIME NOT NULL
);

CREATE TABLE `chat_room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `max_count` int NOT NULL,
  `owner` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL,
  `sender` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `message` text COLLATE utf8mb4_general_ci NOT NULL,
  `sent_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_chat_message_room` (`room_id`),
  CONSTRAINT `fk_chat_message_room`
    FOREIGN KEY (`room_id`)
    REFERENCES `chat_room` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `chat_room_member` (
  `room_id` bigint NOT NULL,
  `nickname` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `joined_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`room_id`, `nickname`),
  CONSTRAINT `fk_chat_room_member_room`
    FOREIGN KEY (`room_id`)
    REFERENCES `chat_room` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

