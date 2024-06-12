DELETE FROM documents
WHERE creator_id IN (SELECT id FROM users WHERE uid = 'hanna_test_1@example.com');

DELETE FROM user_authorities
WHERE user_id IN (SELECT id FROM users WHERE uid = 'hanna_test_1@example.com');

DELETE FROM users
WHERE uid = 'hanna_test_1@example.com';
