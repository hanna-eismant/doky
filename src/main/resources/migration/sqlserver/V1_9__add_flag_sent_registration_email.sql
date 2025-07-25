ALTER TABLE users
    ADD sent_registration_email bit
GO

UPDATE users
    SET sent_registration_email = 1
    WHERE sent_registration_email IS NULL
GO

ALTER TABLE users
    ADD DEFAULT 0 FOR sent_registration_email
GO

ALTER TABLE users
    ALTER COLUMN sent_registration_email bit NOT NULL
GO
