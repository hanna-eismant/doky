CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    uid      VARCHAR(255)          NOT NULL,
    name     VARCHAR(255)          NULL,
    password VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_uid UNIQUE (uid);
