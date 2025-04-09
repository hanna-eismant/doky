ALTER TABLE reset_password_tokens
    DROP FOREIGN KEY FK_RESET_PASSWORD_TOKENS_ON_USER;

ALTER TABLE reset_password_tokens
    ADD app_user BIGINT NULL;

ALTER TABLE reset_password_tokens
    ADD CONSTRAINT FK_RESET_PASSWORD_TOKENS_ON_APP_USER FOREIGN KEY (app_user) REFERENCES users (id);

ALTER TABLE reset_password_tokens
    DROP KEY uc_reset_password_tokens_user;

ALTER TABLE reset_password_tokens
    DROP COLUMN user;

DROP INDEX idx_reset_password_tokens_token ON reset_password_tokens;

CREATE INDEX idx_reset_password_tokens_token ON reset_password_tokens (token);

DROP INDEX idx_users_uid ON users;

CREATE INDEX idx_users_uid ON users (uid);
