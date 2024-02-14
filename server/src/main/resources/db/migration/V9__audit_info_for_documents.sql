ALTER TABLE document
    ADD created_by VARCHAR(255) NULL;

ALTER TABLE document
    ADD created_date BIGINT NULL;

ALTER TABLE document
    ADD modified_by VARCHAR(255) NULL;

ALTER TABLE document
    ADD modified_date BIGINT NULL;

ALTER TABLE document
    MODIFY created_date BIGINT NULL;

UPDATE document SET created_date = 0 WHERE created_date IS NULL;

ALTER TABLE document
    MODIFY created_date BIGINT NOT NULL DEFAULT 0;
