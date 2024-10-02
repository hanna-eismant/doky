INSERT IGNORE INTO reset_password_tokens (token, app_user)
SELECT  "reset-token", u.id FROM users u WHERE u.uid = "hanna_test_1@yopmail.com";
