CREATE TABLE reset_password_token
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    user            BIGINT                NULL,
    token           VARCHAR(255)          NULL,
    expiration_date datetime              NULL,
    CONSTRAINT pk_reset_password_token PRIMARY KEY (id)
);

ALTER TABLE reset_password_token
    ADD CONSTRAINT uc_reset_password_token_user UNIQUE (user);

ALTER TABLE reset_password_token
    ADD CONSTRAINT uq_reset_password_token_token UNIQUE (token);

CREATE INDEX idx_reset_password_token_token ON reset_password_token (token);

ALTER TABLE reset_password_token
    ADD CONSTRAINT FK_RESET_PASSWORD_TOKEN_ON_USER FOREIGN KEY (user) REFERENCES user (id);
