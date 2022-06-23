CREATE TABLE document
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    code             VARCHAR(255)          NOT NULL,
    name             VARCHAR(255)          NULL,
    `description`    LONGTEXT              NULL,
    document_type_id BIGINT                NULL,
    CONSTRAINT pk_document PRIMARY KEY (id)
);

ALTER TABLE document
    ADD CONSTRAINT uc_document_code UNIQUE (code);

ALTER TABLE document
    ADD CONSTRAINT FK_DOCUMENT_ON_DOCUMENT_TYPE FOREIGN KEY (document_type_id) REFERENCES document_type (id);
