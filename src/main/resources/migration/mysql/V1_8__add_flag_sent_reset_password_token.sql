ALTER TABLE reset_password_tokens
    ADD sent_email BIT(1) NULL;

UPDATE reset_password_tokens
SET sent_email = b'0';

ALTER TABLE reset_password_tokens
    MODIFY sent_email BIT(1) NOT NULL;
