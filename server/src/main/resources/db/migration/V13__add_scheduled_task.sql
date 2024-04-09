CREATE TABLE scheduled_task
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    last_run_date datetime              NULL,
    CONSTRAINT pk_scheduled_task PRIMARY KEY (id)
);

ALTER TABLE scheduled_task
    ADD CONSTRAINT uc_scheduled_task_name UNIQUE (name);

-- INSERT INTO scheduled_task (name) VALUES ("solr-index-documents");
