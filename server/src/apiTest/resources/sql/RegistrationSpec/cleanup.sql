DELETE FROM user_authorities
WHERE user_id IN (SELECT id FROM users WHERE uid = 'new_user_test@example.com');

DELETE FROM users
WHERE uid = 'new_user_test@example.com';
