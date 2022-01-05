CREATE TABLE document_type
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    code          VARCHAR(255)          NOT NULL,
    name          VARCHAR(255)          NULL,
    `description` LONGTEXT              NULL,
    CONSTRAINT pk_document_type PRIMARY KEY (id)
);

ALTER TABLE document_type
    ADD CONSTRAINT uc_document_type_code UNIQUE (code);
