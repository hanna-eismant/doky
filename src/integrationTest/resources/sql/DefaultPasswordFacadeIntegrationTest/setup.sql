INSERT IGNORE reset_password_tokens (token, expiration_date, app_user)
SELECT "expired-token", '1924-02-13 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";

INSERT IGNORE INTO users (uid, name, password) VALUES ("hanna_test_2@example.com", "Hanna", "$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m");

INSERT IGNORE reset_password_tokens (token, expiration_date, app_user)
    SELECT "valid-token", '2224-02-13 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_2@example.com";
