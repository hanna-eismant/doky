CREATE TABLE workflow
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    code             VARCHAR(255)          NOT NULL,
    name             VARCHAR(255)          NULL,
    `description`    LONGTEXT              NULL,
    document_type_id BIGINT                NULL,
    CONSTRAINT pk_workflow PRIMARY KEY (id)
);

ALTER TABLE workflow
    ADD CONSTRAINT uc_workflow_code UNIQUE (code);

ALTER TABLE workflow
    ADD CONSTRAINT FK_WORKFLOW_ON_DOCUMENT_TYPE FOREIGN KEY (document_type_id) REFERENCES document_type (id);
