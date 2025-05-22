INSERT IGNORE INTO reset_password_tokens (token, expiration_date, app_user)
SELECT  "reset-token", '2224-02-13 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";
