ALTER TABLE document
    ADD created_by VARCHAR(255) NULL;

ALTER TABLE document
    ADD created_date datetime NULL;

ALTER TABLE document
    ADD modified_by VARCHAR(255) NULL;

ALTER TABLE document
    ADD modified_date datetime NULL;

ALTER TABLE document
    MODIFY created_date datetime NULL;

UPDATE document SET created_date = 0 WHERE created_date IS NULL;

ALTER TABLE document
    MODIFY created_date datetime NOT NULL DEFAULT '1970-01-01 00:00:00';
