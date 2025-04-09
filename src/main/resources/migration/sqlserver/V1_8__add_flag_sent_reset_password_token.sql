ALTER TABLE reset_password_tokens
    ADD sent_email bit
GO

UPDATE reset_password_tokens
    SET sent_email = 0
    WHERE sent_email IS NULL
GO

ALTER TABLE reset_password_tokens
    ADD DEFAULT 0 FOR sent_email
GO

ALTER TABLE reset_password_tokens
    ALTER COLUMN sent_email bit NOT NULL
GO
