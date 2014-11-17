-- liquibase formatted sql

-- ## DO NOT MODIFY THE FIRST LINE OF THIS FILE!
-- ## ADD "runAlways:true" to changeset for apply changes with each run
-- ## more details at http://www.liquibase.org/documentation/sql_format.html

-- changeset oleg.v:1

DROP TABLE IF EXISTS `roadmap`.`auth_token`;
DROP TABLE IF EXISTS `roadmap`.`user_has_application`;
DROP TABLE IF EXISTS `roadmap`.`user`;
DROP TABLE IF EXISTS `roadmap`.`vote`;
DROP TABLE IF EXISTS `roadmap`.`device`;
DROP TABLE IF EXISTS `roadmap`.`survey_has_feature`;
DROP TABLE IF EXISTS `roadmap`.`survey`;
DROP TABLE IF EXISTS `roadmap`.`localized_feature`;
DROP TABLE IF EXISTS `roadmap`.`feature`;
DROP TABLE IF EXISTS `roadmap`.`language`;
DROP TABLE IF EXISTS `roadmap`.`application`;

CREATE TABLE IF NOT EXISTS `roadmap`.`user` (
  `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
  `email`                     VARCHAR(255) NOT NULL,
  `password`                  VARCHAR(255) NOT NULL,
  `disabled`                  BIT          NOT NULL DEFAULT 0,
  `role`                      VARCHAR(255) NOT NULL DEFAULT 'ROLE_USER',
  `recovery_token_expiration` TIMESTAMP    NULL,
  `recovery_token`            VARCHAR(255) NULL,
  `created_at`                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at`               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`auth_token` (
  `token`      VARCHAR(255) NOT NULL,
  `user_id`    BIGINT       NOT NULL,
  `valid_to`   TIMESTAMP    NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token`),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC),
  INDEX `user_tokens_to_users_idx` (`user_id` ASC),
  CONSTRAINT `user_tokens_to_users`
  FOREIGN KEY (`user_id`)
  REFERENCES `roadmap`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`application` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `api_key`     VARCHAR(45)  NOT NULL,
  `api_token`   VARCHAR(45)  NOT NULL,
  `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `api_security_key_UNIQUE` (`api_token` ASC),
  UNIQUE INDEX `api_secret_key_UNIQUE` (`api_key` ASC))
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`feature` (
  `id`             BIGINT    NOT NULL AUTO_INCREMENT,
  `application_id` BIGINT    NOT NULL,
  `implemented`    BIT       NOT NULL DEFAULT 0,
  `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `application_features-to-application_idx` (`application_id` ASC),
  CONSTRAINT `application_features-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `roadmap`.`application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`survey` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT,
  `title`          VARCHAR(255) NOT NULL,
  `application_id` BIGINT       NOT NULL,
  `active`         BIT          NOT NULL DEFAULT 0,
  `required_votes` BIGINT       NULL DEFAULT NULL,
  `finish_at`      TIMESTAMP    NULL,
  `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `surveys-to_applications_idx` (`application_id` ASC),
  CONSTRAINT `surveys-to-applications`
  FOREIGN KEY (`application_id`)
  REFERENCES `roadmap`.`application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`survey_has_feature` (
  `feature_id` BIGINT NOT NULL,
  `survey_id`  BIGINT NOT NULL,
  PRIMARY KEY (`survey_id`, `feature_id`),
  INDEX `survey_features-to-app_features_idx` (`feature_id` ASC),
  CONSTRAINT `survey_features-to-surveys`
  FOREIGN KEY (`survey_id`)
  REFERENCES `roadmap`.`survey` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `survey_features-to-app_features`
  FOREIGN KEY (`feature_id`)
  REFERENCES `roadmap`.`feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`device` (
  `token`         VARCHAR(255) NOT NULL,
  `platform`      INT          NOT NULL,
  `device_locale` VARCHAR(10)  NOT NULL,
  PRIMARY KEY (`token`),
  UNIQUE INDEX `device_token_UNIQUE` (`token` ASC))
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`language` (
  `id`   INT          NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `code` VARCHAR(10)  NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`vote` (
  `uuid`         VARCHAR(45)  NOT NULL,
  `device_token` VARCHAR(255) NOT NULL,
  `survey_id`    BIGINT       NOT NULL,
  `feature_id`   BIGINT       NOT NULL,
  `language_id`  INT          NOT NULL,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `votes-to-surveys_idx` (`survey_id` ASC),
  INDEX `votes-to-app_features_idx` (`feature_id` ASC),
  INDEX `votes-to-devices_idx` (`device_token` ASC),
  INDEX `votes-to-language_idx` (`language_id` ASC),
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC),
  CONSTRAINT `votes-to-surveys`
  FOREIGN KEY (`survey_id`)
  REFERENCES `roadmap`.`survey` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `votes-to-app_features`
  FOREIGN KEY (`feature_id`)
  REFERENCES `roadmap`.`feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `votes-to-devices`
  FOREIGN KEY (`device_token`)
  REFERENCES `roadmap`.`device` (`token`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `votes-to-language`
  FOREIGN KEY (`language_id`)
  REFERENCES `roadmap`.`language` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`localized_feature` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT,
  `feature_id`  BIGINT      NOT NULL,
  `language_id` INT         NOT NULL,
  `title`       VARCHAR(45) NOT NULL,
  `created_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `localized_features-to-features_idx` (`feature_id` ASC),
  INDEX `localized_features-to-language_idx` (`language_id` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  CONSTRAINT `localized_features-to-feature`
  FOREIGN KEY (`feature_id`)
  REFERENCES `roadmap`.`feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `localized_features-to-language`
  FOREIGN KEY (`language_id`)
  REFERENCES `roadmap`.`language` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roadmap`.`user_has_application` (
  `id`             BIGINT    NOT NULL AUTO_INCREMENT,
  `user_id`        BIGINT    NOT NULL,
  `application_id` BIGINT    NOT NULL,
  `access_level`   TINYINT   NOT NULL DEFAULT 0,
  `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `user_to_application-to-application_idx` (`application_id` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  CONSTRAINT `user_to_application-to-user`
  FOREIGN KEY (`user_id`)
  REFERENCES `roadmap`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_to_application-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `roadmap`.`application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Data for table `roadmap`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `roadmap`;
INSERT INTO `roadmap`.`user` (`id`, `email`, `password`, `disabled`, `role`, `recovery_token_expiration`, `recovery_token`, `created_at`, `modified_at`)
VALUES (1, 'testuser1@mail.com', '123', 0, 'ROLE_USER', NULL, NULL, NULL, NULL);
INSERT INTO `roadmap`.`user` (`id`, `email`, `password`, `disabled`, `role`, `recovery_token_expiration`, `recovery_token`, `created_at`, `modified_at`)
VALUES (2, 'testuser2@mail.com', '123', 0, 'ROLE_USER', NULL, NULL, NULL, NULL);
INSERT INTO `roadmap`.`user` (`id`, `email`, `password`, `disabled`, `role`, `recovery_token_expiration`, `recovery_token`, `created_at`, `modified_at`)
VALUES (3, 'testuser3@mail.com', '123', 0, 'ROLE_USER', NULL, NULL, NULL, NULL);

COMMIT;

-- -----------------------------------------------------
-- Data for table `roadmap`.`application`
-- -----------------------------------------------------
START TRANSACTION;
USE `roadmap`;
INSERT INTO `roadmap`.`application` (`id`, `name`, `description`, `api_key`, `api_token`, `created_at`, `modified_at`)
VALUES (1, 'App1', 'Description1', 'key1', 'token1', NULL, NULL);
INSERT INTO `roadmap`.`application` (`id`, `name`, `description`, `api_key`, `api_token`, `created_at`, `modified_at`)
VALUES (2, 'App2', 'Description2', 'key2', 'token2', NULL, NULL);
INSERT INTO `roadmap`.`application` (`id`, `name`, `description`, `api_key`, `api_token`, `created_at`, `modified_at`)
VALUES (3, 'App3', 'Description3', 'key3', 'token3', NULL, NULL);

COMMIT;

-- -----------------------------------------------------
-- Data for table `roadmap`.`language`
-- -----------------------------------------------------
START TRANSACTION;
USE `roadmap`;
INSERT INTO `roadmap`.`language` (`id`, `name`, `code`) VALUES (1, 'English', 'en_US');

COMMIT;

-- -----------------------------------------------------
-- Data for table `roadmap`.`user_has_application`
-- -----------------------------------------------------
START TRANSACTION;
USE `roadmap`;
INSERT INTO `roadmap`.`user_has_application` (`id`, `user_id`, `application_id`, `access_level`, `created_at`, `modified_at`)
VALUES (1, 1, 1, 0, NULL, NULL);
INSERT INTO `roadmap`.`user_has_application` (`id`, `user_id`, `application_id`, `access_level`, `created_at`, `modified_at`)
VALUES (2, 1, 2, 0, NULL, NULL);
INSERT INTO `roadmap`.`user_has_application` (`id`, `user_id`, `application_id`, `access_level`, `created_at`, `modified_at`)
VALUES (3, 2, 3, 0, NULL, NULL);

COMMIT;

