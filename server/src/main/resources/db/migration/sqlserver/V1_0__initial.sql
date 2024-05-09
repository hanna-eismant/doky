CREATE TABLE authorities
(
    id        bigint IDENTITY (1, 1) NOT NULL,
    authority varchar(255)           NOT NULL,
    CONSTRAINT pk_authorities PRIMARY KEY (id)
)
GO

CREATE TABLE documents
(
    id            bigint IDENTITY (1, 1) NOT NULL,
    created_date  datetime               NOT NULL,
    created_by    bigint,
    modified_date datetime,
    modified_by   bigint,
    name          varchar(255)           NOT NULL,
    description   varchar(MAX),
    file_path     varchar(255),
    file_name     varchar(255),
    creator_id    bigint,
    CONSTRAINT pk_documents PRIMARY KEY (id)
)
GO

CREATE TABLE reset_password_tokens
(
    id              bigint IDENTITY (1, 1) NOT NULL,
    [user]          bigint,
    token           varchar(255),
    expiration_date datetime,
    CONSTRAINT pk_reset_password_tokens PRIMARY KEY (id)
)
GO

CREATE TABLE user_authorities
(
    authority_id bigint NOT NULL,
    user_id      bigint NOT NULL,
    CONSTRAINT pk_user_authorities PRIMARY KEY (authority_id, user_id)
)
GO

CREATE TABLE users
(
    id       bigint IDENTITY (1, 1) NOT NULL,
    uid      varchar(255)           NOT NULL,
    name     varchar(255),
    password varchar(255)           NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
)
GO

ALTER TABLE authorities
    ADD CONSTRAINT uc_authorities_authority UNIQUE (authority)
GO

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT uc_reset_password_tokens_token UNIQUE (token)
GO

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT uc_reset_password_tokens_user UNIQUE ([user])
GO

ALTER TABLE users
    ADD CONSTRAINT uc_users_uid UNIQUE (uid)
GO

CREATE NONCLUSTERED INDEX idx_reset_password_tokens_token ON reset_password_tokens (token)
GO

CREATE NONCLUSTERED INDEX idx_users_uid ON users (uid)
GO

ALTER TABLE documents
    ADD CONSTRAINT FK_DOCUMENTS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id)
GO

ALTER TABLE documents
    ADD CONSTRAINT FK_DOCUMENTS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id)
GO

ALTER TABLE documents
    ADD CONSTRAINT FK_DOCUMENTS_ON_MODIFIED_BY FOREIGN KEY (modified_by) REFERENCES users (id)
GO

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT FK_RESET_PASSWORD_TOKENS_ON_USER FOREIGN KEY ([user]) REFERENCES users (id)
GO

ALTER TABLE user_authorities
    ADD CONSTRAINT fk_useaut_on_authority_entity FOREIGN KEY (authority_id) REFERENCES authorities (id)
GO

ALTER TABLE user_authorities
    ADD CONSTRAINT fk_useaut_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id)
GO
