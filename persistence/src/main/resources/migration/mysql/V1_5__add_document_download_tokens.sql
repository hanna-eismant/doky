CREATE TABLE download_document_tokens
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    app_user        BIGINT                NOT NULL,
    document        BIGINT                NOT NULL,
    token           VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_download_document_tokens PRIMARY KEY (id)
);

ALTER TABLE download_document_tokens
    ADD CONSTRAINT uc_download_document_tokens_app_user_document UNIQUE (app_user, document);

ALTER TABLE download_document_tokens
    ADD CONSTRAINT uc_download_document_tokens_token UNIQUE (token);

CREATE INDEX idx_download_document_tokens_token ON download_document_tokens (token);

ALTER TABLE download_document_tokens
    ADD CONSTRAINT FK_DOWNLOAD_DOCUMENT_TOKENS_ON_APP_USER FOREIGN KEY (app_user) REFERENCES users (id);

ALTER TABLE download_document_tokens
    ADD CONSTRAINT FK_DOWNLOAD_DOCUMENT_TOKENS_ON_DOCUMENT FOREIGN KEY (document) REFERENCES documents (id);

