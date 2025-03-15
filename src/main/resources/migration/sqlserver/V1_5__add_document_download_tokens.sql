CREATE TABLE download_document_tokens
(
    id              bigint IDENTITY (1, 1) NOT NULL,
    app_user        bigint                 NOT NULL,
    document        bigint                 NOT NULL,
    token           varchar(255)           NOT NULL,
    CONSTRAINT pk_download_document_tokens PRIMARY KEY (id)
)
GO

ALTER TABLE download_document_tokens
    ADD CONSTRAINT uc_download_document_tokens_app_user_document UNIQUE (app_user, document)
GO

ALTER TABLE download_document_tokens
    ADD CONSTRAINT uc_download_document_tokens_token UNIQUE (token)
GO

CREATE NONCLUSTERED INDEX idx_download_document_tokens_token ON download_document_tokens (token)
GO

ALTER TABLE download_document_tokens
    ADD CONSTRAINT FK_DOWNLOAD_DOCUMENT_TOKENS_ON_APP_USER FOREIGN KEY (app_user) REFERENCES users (id)
GO

ALTER TABLE download_document_tokens
    ADD CONSTRAINT FK_DOWNLOAD_DOCUMENT_TOKENS_ON_DOCUMENT FOREIGN KEY (document) REFERENCES documents (id)
GO
