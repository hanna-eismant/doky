ALTER TABLE document
    ADD file_name VARCHAR(255) NULL;

UPDATE document AS d
    SET file_name = REVERSE(LEFT(REVERSE(d.file_path), POSITION('/' IN REVERSE(d.file_path)) -1))
    WHERE file_name IS NULL;
