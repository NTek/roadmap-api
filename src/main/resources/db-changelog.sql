-- liquibase formatted sql

-- ## DO NOT MODIFY THE FIRST LINE OF THIS FILE!
-- ## ADD "runAlways:true" to changeset for apply changes with each run
-- ## more details at http://www.liquibase.org/documentation/sql_format.html

-- changeset oleg.v:1
DROP TABLE IF EXISTS `user_has_application`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `vote`;
DROP TABLE IF EXISTS `device`;
DROP TABLE IF EXISTS `survey_has_feature`;
DROP TABLE IF EXISTS `survey`;
DROP TABLE IF EXISTS `localized_feature`;
DROP TABLE IF EXISTS `feature_text`;
DROP TABLE IF EXISTS `feature`;
DROP TABLE IF EXISTS `language`;
DROP TABLE IF EXISTS `application`;

CREATE TABLE IF NOT EXISTS `user` (
  `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
  `uuid`                      BINARY(16)   NULL,
  `email`                     VARCHAR(255) NOT NULL,
  `password`                  VARCHAR(255) NOT NULL,
  `enabled`                   BIT          NOT NULL DEFAULT 1,
  `role`                      VARCHAR(255) NOT NULL DEFAULT 'ROLE_USER',
  `recovery_token_expiration` TIMESTAMP    NULL,
  `recovery_token`            VARCHAR(255) NULL,
  `modified_at`               TIMESTAMP    NULL,
  `created_at`                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `uid_UNIQUE` (`uuid` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)
)

  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `application` (
  `id`               BIGINT       NOT NULL AUTO_INCREMENT,
  `uuid`             BINARY(16)   NULL,
  `name`             VARCHAR(255) NOT NULL,
  `description`      VARCHAR(255) NULL,
  `api_token`        VARCHAR(45)  NOT NULL,
  `active_survey_id` BIGINT       NULL,
  `modified_at`      TIMESTAMP    NULL,
  `created_at`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `uid_UNIQUE` (`uuid` ASC),
  UNIQUE INDEX `api_security_key_UNIQUE` (`api_token` ASC),
  UNIQUE INDEX `active_survey_id_UNIQUE` (`active_survey_id` ASC)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `feature` (
  `id`             BIGINT    NOT NULL AUTO_INCREMENT,
  `application_id` BIGINT    NOT NULL,
  `implemented`    BIT       NOT NULL DEFAULT 0,
  `modified_at`    TIMESTAMP NULL,
  `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `application_features-to-application_idx` (`application_id` ASC),
  CONSTRAINT `application_features-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `survey` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT,
  `uuid`           BINARY(16)   NULL,
  `title`          VARCHAR(255) NOT NULL,
  `application_id` BIGINT       NOT NULL,
  `disabled`       BIT          NOT NULL DEFAULT 0,
  `required_votes` BIGINT       NULL,
  `required_date`  TIMESTAMP    NULL,
  `started_at`     TIMESTAMP    NULL,
  `finished_at`    TIMESTAMP    NULL,
  `modified_at`    TIMESTAMP    NULL,
  `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `uid_UNIQUE` (`uuid` ASC),
  INDEX `surveys-to_applications_idx` (`application_id` ASC),
  CONSTRAINT `survey-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

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
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `vote` (
  `device_token` VARCHAR(255) NOT NULL,
  `survey_id`    BIGINT       NOT NULL,
  `feature_id`   BIGINT       NOT NULL,
  `language`     VARCHAR(2)   NULL,
  `platform`     VARCHAR(50)  NULL,
  `modified_at`  TIMESTAMP    NULL,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `votes-to-surveys_idx` (`survey_id` ASC),
  INDEX `votes-to-app_features_idx` (`feature_id` ASC),
  PRIMARY KEY (`device_token`, `survey_id`),
  CONSTRAINT `votes-to-surveys`
  FOREIGN KEY (`survey_id`)
  REFERENCES `survey` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `votes-to-app_features`
  FOREIGN KEY (`feature_id`)
  REFERENCES `feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `feature_text` (
  `feature_id`  BIGINT       NOT NULL,
  `language`    VARCHAR(2)   NOT NULL DEFAULT 'en',
  `text`        VARCHAR(255) NOT NULL,
  `modified_at` TIMESTAMP    NULL,
  `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `localized_features-to-features_idx` (`feature_id` ASC),
  PRIMARY KEY (`feature_id`, `language`),
  CONSTRAINT `localized_features-to-feature`
  FOREIGN KEY (`feature_id`)
  REFERENCES `feature` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_has_application` (
  `user_id`        BIGINT    NOT NULL,
  `application_id` BIGINT    NOT NULL,
  `access_level`   TINYINT   NOT NULL DEFAULT 0,
  `modified_at`    TIMESTAMP NULL,
  `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `user_to_application-to-application_idx` (`application_id` ASC),
  PRIMARY KEY (`user_id`, `application_id`),
  CONSTRAINT `user_to_application-to-user`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_to_application-to-application`
  FOREIGN KEY (`application_id`)
  REFERENCES `application` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Data for table `user`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `user` (`id`, `email`, `password`, `enabled`, `role`, `recovery_token_expiration`, `recovery_token`, `modified_at`, `created_at`)
VALUES (1, 'testuser1@mail.com', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', 1, 'ROLE_USER', NULL, NULL, NULL, NULL);
INSERT INTO `user` (`id`, `email`, `password`, `enabled`, `role`, `recovery_token_expiration`, `recovery_token`, `modified_at`, `created_at`)
VALUES (2, 'testuser2@mail.com', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', 1, 'ROLE_USER', NULL, NULL, NULL, NULL);
INSERT INTO `user` (`id`, `email`, `password`, `enabled`, `role`, `recovery_token_expiration`, `recovery_token`, `modified_at`, `created_at`)
VALUES (3, 'testuser3@mail.com', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', 1, 'ROLE_USER', NULL, NULL, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `application`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `application` (`id`, `name`, `description`, `api_token`, `active_survey_id`, `modified_at`, `created_at`)
VALUES (1, 'App1', 'Description1', 'token1', 1, NULL, NULL);
INSERT INTO `application` (`id`, `name`, `description`, `api_token`, `active_survey_id`, `modified_at`, `created_at`)
VALUES (2, 'App2', 'Description2', 'token2', 2, NULL, NULL);
INSERT INTO `application` (`id`, `name`, `description`, `api_token`, `active_survey_id`, `modified_at`, `created_at`)
VALUES (3, 'App3', 'Description3', 'token3', NULL, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `feature`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `feature` (`id`, `application_id`, `implemented`, `modified_at`, `created_at`)
VALUES (1, 1, 0, NULL, NULL);
INSERT INTO `feature` (`id`, `application_id`, `implemented`, `modified_at`, `created_at`)
VALUES (2, 1, 0, NULL, NULL);
INSERT INTO `feature` (`id`, `application_id`, `implemented`, `modified_at`, `created_at`)
VALUES (3, 1, 0, NULL, NULL);
INSERT INTO `feature` (`id`, `application_id`, `implemented`, `modified_at`, `created_at`)
VALUES (4, 2, 0, NULL, NULL);
INSERT INTO `feature` (`id`, `application_id`, `implemented`, `modified_at`, `created_at`)
VALUES (5, 2, 0, NULL, NULL);
INSERT INTO `feature` (`id`, `application_id`, `implemented`, `modified_at`, `created_at`)
VALUES (6, 3, 0, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `survey`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `survey` (`id`, `title`, `application_id`, `disabled`, `required_votes`, `required_date`, `started_at`, `finished_at`, `modified_at`, `created_at`)
VALUES (1, 'Survey1', 1, 0, 1000, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `survey` (`id`, `title`, `application_id`, `disabled`, `required_votes`, `required_date`, `started_at`, `finished_at`, `modified_at`, `created_at`)
VALUES (2, 'Sunvey2', 2, 0, 20, NULL, NULL, NULL, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `survey_has_feature`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `survey_has_feature` (`feature_id`, `survey_id`) VALUES (1, 1);
INSERT INTO `survey_has_feature` (`feature_id`, `survey_id`) VALUES (2, 1);
INSERT INTO `survey_has_feature` (`feature_id`, `survey_id`) VALUES (3, 1);
INSERT INTO `survey_has_feature` (`feature_id`, `survey_id`) VALUES (4, 2);
INSERT INTO `survey_has_feature` (`feature_id`, `survey_id`) VALUES (5, 2);

COMMIT;


-- -----------------------------------------------------
-- Data for table `feature_text`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (1, 'en', 'Feature 1 en-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (2, 'en', 'Feature 2 en-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (3, 'en', 'Feature 3 en-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (4, 'en', 'Feature 4 en-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (5, 'en', 'Feature 5 en-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (6, 'en', 'Feature 6 en-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (1, 'ru', 'Feature 1 ru-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (2, 'ru', 'Feature 2 ru-text', NULL, NULL);
INSERT INTO `feature_text` (`feature_id`, `language`, `text`, `modified_at`, `created_at`)
VALUES (3, 'ru', 'Feature 3 ru-text', NULL, NULL);
COMMIT;


-- -----------------------------------------------------
-- Data for table `user_has_application`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `user_has_application` (`user_id`, `application_id`, `access_level`, `modified_at`, `created_at`)
VALUES (1, 1, 0, NULL, NULL);
INSERT INTO `user_has_application` (`user_id`, `application_id`, `access_level`, `modified_at`, `created_at`)
VALUES (1, 2, 0, NULL, NULL);
INSERT INTO `user_has_application` (`user_id`, `application_id`, `access_level`, `modified_at`, `created_at`)
VALUES (2, 3, 0, NULL, NULL);
COMMIT;
