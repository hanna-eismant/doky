ALTER TABLE users
    ADD COLUMN sent_registration_email BIT(1) NULL;

UPDATE users
SET sent_registration_email = b'1';

ALTER TABLE users
    MODIFY COLUMN sent_registration_email BIT(1) NOT NULL DEFAULT b'0';
