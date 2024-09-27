ALTER TABLE reset_password_tokens
    DROP CONSTRAINT FK_RESET_PASSWORD_TOKENS_ON_USER
GO

ALTER TABLE reset_password_tokens
    ADD app_user bigint
GO

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT uc_reset_password_tokens_app_user UNIQUE (app_user)
GO

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT FK_RESET_PASSWORD_TOKENS_ON_APP_USER FOREIGN KEY (app_user) REFERENCES users (id)
GO

ALTER TABLE reset_password_tokens
    DROP CONSTRAINT uc_reset_password_tokens_user
GO

DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE reset_password_tokens DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'reset_password_tokens')
  AND [c].[name] = N'[user]'
EXEC sp_executesql @sql
GO

ALTER TABLE reset_password_tokens
    DROP COLUMN [user]
GO
