UPDATE document AS d SET d.name = CONCAT('Document ', d.id) WHERE d.name IS NULL OR d.name = '';

ALTER TABLE document
    MODIFY name VARCHAR(255) NOT NULL;
