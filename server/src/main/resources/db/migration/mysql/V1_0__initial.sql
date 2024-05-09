CREATE TABLE authorities
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    authority VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_authorities PRIMARY KEY (id)
);

CREATE TABLE documents
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_date  datetime              NOT NULL,
    created_by    BIGINT                NULL,
    modified_date datetime              NULL,
    modified_by   BIGINT                NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` LONGTEXT              NULL,
    file_path     VARCHAR(255)          NULL,
    file_name     VARCHAR(255)          NULL,
    creator_id    BIGINT                NULL,
    CONSTRAINT pk_documents PRIMARY KEY (id)
);

CREATE TABLE reset_password_tokens
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    user            BIGINT                NULL,
    token           VARCHAR(255)          NULL,
    expiration_date datetime              NULL,
    CONSTRAINT pk_reset_password_tokens PRIMARY KEY (id)
);

CREATE TABLE user_authorities
(
    authority_id BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    CONSTRAINT pk_user_authorities PRIMARY KEY (authority_id, user_id)
);

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    uid      VARCHAR(255)          NOT NULL,
    name     VARCHAR(255)          NULL,
    password VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE authorities
    ADD CONSTRAINT uc_authorities_authority UNIQUE (authority);

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT uc_reset_password_tokens_token UNIQUE (token);

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT uc_reset_password_tokens_user UNIQUE (user);

ALTER TABLE users
    ADD CONSTRAINT uc_users_uid UNIQUE (uid);

CREATE INDEX idx_reset_password_tokens_token ON reset_password_tokens (token);

CREATE INDEX idx_users_uid ON users (uid);

ALTER TABLE documents
    ADD CONSTRAINT FK_DOCUMENTS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE documents
    ADD CONSTRAINT FK_DOCUMENTS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

ALTER TABLE documents
    ADD CONSTRAINT FK_DOCUMENTS_ON_MODIFIED_BY FOREIGN KEY (modified_by) REFERENCES users (id);

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT FK_RESET_PASSWORD_TOKENS_ON_USER FOREIGN KEY (user) REFERENCES users (id);

ALTER TABLE user_authorities
    ADD CONSTRAINT fk_useaut_on_authority_entity FOREIGN KEY (authority_id) REFERENCES authorities (id);

ALTER TABLE user_authorities
    ADD CONSTRAINT fk_useaut_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id);
