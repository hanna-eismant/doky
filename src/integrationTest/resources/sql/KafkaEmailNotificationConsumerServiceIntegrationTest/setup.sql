INSERT IGNORE INTO reset_password_tokens (token, expiration_date, app_user)
SELECT  "reset-token", "2032-06-16 16:37:23", u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";
