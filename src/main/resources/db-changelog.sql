-- liquibase formatted sql

-- ## DO NOT MODIFY THE FIRST LINE OF THIS FILE!
-- ## ADD "runAlways:true" to changeset for apply changes with each run
-- ## more details at http://www.liquibase.org/documentation/sql_format.html

-- changeset oleg.v:1 runAlways:true

DROP TABLE IF EXISTS `auth_token`;
DROP TABLE IF EXISTS `user_has_application`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `vote`;
DROP TABLE IF EXISTS `device`;
DROP TABLE IF EXISTS `survey_has_feature`;
DROP TABLE IF EXISTS `survey`;
DROP TABLE IF EXISTS `localized_feature`;
DROP TABLE IF EXISTS `feature`;
DROP TABLE IF EXISTS `language`;
DROP TABLE IF EXISTS `application`;

CREATE TABLE IF NOT EXISTS `user` (
  `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
  `email`                     VARCHAR(255) NOT NULL,
  `password`                  VARCHAR(255) NOT NULL,
  `disabled`                  BIT          NOT NULL DEFAULT 0,
  `role`                      VARCHAR(255) NOT NULL DEFAULT 'ROLE_USER',
  `recovery_token_expiration` TIMESTAMP    NULL,
  `recovery_token`            VARCHAR(255) NULL,
  `modified_at`               TIMESTAMP    NULL,
  `created_at`                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `auth_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `auth_token` (
  `token`       VARCHAR(255) NOT NULL,
  `user_id`     BIGINT       NOT NULL,
  `valid_to`    TIMESTAMP    NULL,
  `modified_at` TIMESTAMP    NULL,
  `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token`),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC),
  INDEX `user_tokens_to_users_idx` (`user_id` ASC),
  CONSTRAINT `user_tokens_to_users`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `application` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `api_key`     VARCHAR(45)  NOT NULL,
  `api_token`   VARCHAR(45)  NOT NULL,
  `modified_at` TIMESTAMP    NULL,
  `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `api_security_key_UNIQUE` (`api_token` ASC),
  UNIQUE INDEX `api_secret_key_UNIQUE` (`api_key` ASC))
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `feature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `feature` (
  `id`             BIGINT    NOT NULL AUTO_INCREMENT,
  `application_id` BIGINT    NOT NULL,
  `implemented`    BIT       NOT NULL DEFAULT 0,
  `modified_at`    TIMESTAMP NULL,
  `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `application_features-to-apllication_idx` (`application_id` ASC),
  CONSTRAINT `application_features-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `survey`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `survey` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT,
  `title`          VARCHAR(255) NOT NULL,
  `application_id` BIGINT       NOT NULL,
  `active`         BIT          NOT NULL DEFAULT 0,
  `required_votes` BIGINT       NULL DEFAULT NULL,
  `finish_at`      TIMESTAMP    NULL,
  `modified_at`    TIMESTAMP    NULL,
  `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `surveys-to_applications_idx` (`application_id` ASC),
  CONSTRAINT `surveys-to-applications`
  FOREIGN KEY (`application_id`)
  REFERENCES `application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `survey_has_feature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `survey_has_feature` (
  `feature_id` BIGINT NOT NULL,
  `survey_id`  BIGINT NOT NULL,
  PRIMARY KEY (`survey_id`, `feature_id`),
  INDEX `survey_features-to-app_features_idx` (`feature_id` ASC),
  CONSTRAINT `survey_features-to-surveys`
  FOREIGN KEY (`survey_id`)
  REFERENCES `survey` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `survey_features-to-app_features`
  FOREIGN KEY (`feature_id`)
  REFERENCES `feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `device`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `device` (
  `token`         VARCHAR(255) NOT NULL,
  `platform`      INT          NOT NULL,
  `device_locale` VARCHAR(10)  NOT NULL,
  PRIMARY KEY (`token`),
  UNIQUE INDEX `device_token_UNIQUE` (`token` ASC))
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `vote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `roadmap`.`vote` (
  `uuid`         VARCHAR(45)  NOT NULL,
  `device_token` VARCHAR(255) NOT NULL,
  `survey_id`    BIGINT       NOT NULL,
  `feature_id`   BIGINT       NOT NULL,
  `language`     VARCHAR(2)   NULL,
  `modified_at`  TIMESTAMP    NULL,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `votes-to-surveys_idx` (`survey_id` ASC),
  INDEX `votes-to-app_features_idx` (`feature_id` ASC),
  INDEX `votes-to-devices_idx` (`device_token` ASC),
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
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `localized_feature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `roadmap`.`localized_feature` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT,
  `feature_id`  BIGINT      NOT NULL,
  `language`    VARCHAR(2)  NOT NULL DEFAULT 'en',
  `title`       VARCHAR(45) NOT NULL,
  `modified_at` TIMESTAMP   NULL,
  `created_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `localized_features-to-features_idx` (`feature_id` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  CONSTRAINT `localized_features-to-feature`
  FOREIGN KEY (`feature_id`)
  REFERENCES `roadmap`.`feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user_has_application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_has_application` (
  `id`             BIGINT    NOT NULL AUTO_INCREMENT,
  `user_id`        BIGINT    NOT NULL,
  `application_id` BIGINT    NOT NULL,
  `access_level`   TINYINT   NOT NULL DEFAULT 0,
  `modified_at`    TIMESTAMP NULL,
  `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `user_to_application-to-application_idx` (`application_id` ASC),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  CONSTRAINT `user_to_application-to-user`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_to_application-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Data for table `user`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `user` (`id`, `email`, `password`, `disabled`, `role`, `recovery_token_expiration`, `recovery_token`, `modified_at`, `created_at`)
VALUES (1, 'testuser1@mail.com', '123', 0, 'ROLE_USER', NULL, NULL, NULL, NULL);
INSERT INTO `user` (`id`, `email`, `password`, `disabled`, `role`, `recovery_token_expiration`, `recovery_token`, `modified_at`, `created_at`)
VALUES (2, 'testuser2@mail.com', '123', 0, 'ROLE_USER', NULL, NULL, NULL, NULL);
INSERT INTO `user` (`id`, `email`, `password`, `disabled`, `role`, `recovery_token_expiration`, `recovery_token`, `modified_at`, `created_at`)
VALUES (3, 'testuser3@mail.com', '123', 0, 'ROLE_USER', NULL, NULL, NULL, NULL);
COMMIT;

-- -----------------------------------------------------
-- Data for table `application`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `application` (`id`, `name`, `description`, `api_key`, `api_token`, `modified_at`, `created_at`)
VALUES (1, 'App1', 'Description1', 'key1', 'token1', NULL, NULL);
INSERT INTO `application` (`id`, `name`, `description`, `api_key`, `api_token`, `modified_at`, `created_at`)
VALUES (2, 'App2', 'Description2', 'key2', 'token2', NULL, NULL);
INSERT INTO `application` (`id`, `name`, `description`, `api_key`, `api_token`, `modified_at`, `created_at`)
VALUES (3, 'App3', 'Description3', 'key3', 'token3', NULL, NULL);
COMMIT;

-- -----------------------------------------------------
-- Data for table `user_has_application`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `user_has_application` (`id`, `user_id`, `application_id`, `access_level`, `modified_at`, `created_at`)
VALUES (1, 1, 1, 0, NULL, NULL);
INSERT INTO `user_has_application` (`id`, `user_id`, `application_id`, `access_level`, `modified_at`, `created_at`)
VALUES (2, 1, 2, 0, NULL, NULL);
INSERT INTO `user_has_application` (`id`, `user_id`, `application_id`, `access_level`, `modified_at`, `created_at`)
VALUES (3, 2, 3, 0, NULL, NULL);
COMMIT;


