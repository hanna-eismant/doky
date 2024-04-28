RENAME TABLE user TO users;
ALTER TABLE users DROP KEY uc_user_uid;
ALTER TABLE users ADD CONSTRAINT uc_users_uid UNIQUE (uid);
ALTER TABLE users DROP INDEX idx_userentity_uid;
CREATE INDEX idx_users_uid ON users (uid);

RENAME TABLE document TO documents;

RENAME TABLE reset_password_token TO reset_password_tokens;
ALTER TABLE reset_password_tokens DROP KEY uq_reset_password_token_token;
ALTER TABLE reset_password_tokens ADD CONSTRAINT uc_reset_password_tokens_token UNIQUE (token);
ALTER TABLE reset_password_tokens ADD CONSTRAINT uc_reset_password_tokens_user UNIQUE (user);
ALTER TABLE reset_password_tokens DROP KEY uc_reset_password_token_user;
ALTER TABLE reset_password_tokens DROP INDEX idx_reset_password_token_token;
CREATE INDEX idx_reset_password_tokens_token ON reset_password_tokens (token);

RENAME TABLE scheduled_task TO scheduled_tasks;
