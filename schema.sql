CREATE DATABASE IF NOT EXISTS voting_system
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE voting_system;

CREATE TABLE IF NOT EXISTS voter_register (
  uid BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  surname VARCHAR(100) NOT NULL,
  voter_card_number VARCHAR(50) NOT NULL,
  contact VARCHAR(20) NOT NULL,
  address VARCHAR(255) NOT NULL,
  dob DATE NOT NULL,
  email VARCHAR(150) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (uid),
  UNIQUE KEY uk_voter_register_voter_card_number (voter_card_number),
  UNIQUE KEY uk_voter_register_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS temp_voter_card_number (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  voter_card_number VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_temp_voter_card_number (voter_card_number)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vote (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  voter_card_number VARCHAR(50) NOT NULL,
  partie VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_vote_partie (partie),
  CONSTRAINT fk_vote_voter_card
    FOREIGN KEY (voter_card_number) REFERENCES voter_register(voter_card_number)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS admin_login (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_admin_login_user_name (user_name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS contact (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  pnumber VARCHAR(20) NOT NULL,
  email VARCHAR(150) NOT NULL,
  comment TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO admin_login (user_name, password)
VALUES ('admin', 'CHANGE_ME_STRONG_PASSWORD')
ON DUPLICATE KEY UPDATE password = VALUES(password);
