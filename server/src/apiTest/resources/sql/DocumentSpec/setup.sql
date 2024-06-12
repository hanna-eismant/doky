-- INSERT IGNORE INTO documents (name, description, file_name, created_date, modified_date, creator_id)
-- SELECT "Test_1", "That is a test document", "test.txt", '2024-01-15 13:00:00', '2024-01-15 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";
--
-- INSERT IGNORE INTO documents (name, description, created_date, modified_date, creator_id)
-- SELECT "Test_2", "That is a second test document", '2024-01-15 16:00:00', '2024-01-15 16:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";
--
-- INSERT IGNORE INTO users (uid, name, password) VALUES ( "hanna_test_2@example.com", "Hanna", "$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m" );
--
-- INSERT IGNORE INTO user_authorities (user_id, authority_id)
-- SELECT u.id, a.id
-- FROM users u, authorities a
-- WHERE a.authority = 'ROLE_USER' AND u.uid = "hanna_test_2@example.com";
--
-- INSERT IGNORE INTO documents (name, description, created_date, modified_date, creator_id)
-- SELECT "Test_3", "That is a test document", '2024-02-13 13:00:00', '2024-03-15 13:24:00', u.id FROM users u WHERE u.uid = "hanna_test_2@example.com";
--
-- INSERT IGNORE INTO documents (name, description, created_date, modified_date, creator_id)
-- SELECT "Test_4", "That is a second test document", '2024-01-15 13:00:00', '2024-01-15 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_2@example.com";




INSERT INTO documents (name, description, file_name, created_date, modified_date, creator_id)
SELECT 'Test_1', 'That is a test document', 'test.txt', '2024-01-15T13:00:00', '2024-01-15T13:00:00', u.id
FROM users u
WHERE u.uid = 'hanna_test_1@example.com'
AND NOT EXISTS (SELECT 1 FROM documents d WHERE d.name = 'Test_1' AND d.creator_id = u.id);

INSERT INTO documents (name, description, created_date, modified_date, creator_id)
SELECT 'Test_2', 'That is a second test document', '2024-01-15T16:00:00', '2024-01-15T16:00:00', u.id
FROM users u
WHERE u.uid = 'hanna_test_1@example.com'
AND NOT EXISTS (SELECT 1 FROM documents d WHERE d.name = 'Test_2' AND d.creator_id = u.id);

IF NOT EXISTS (SELECT 1 FROM users WHERE uid = 'hanna_test_2@example.com')
  INSERT INTO users (uid, name, password)
  VALUES ('hanna_test_2@example.com', 'Hanna', '$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m');

INSERT INTO user_authorities (user_id, authority_id)
SELECT u.id, a.id
FROM users u
CROSS JOIN authorities a
WHERE a.authority = 'ROLE_USER' AND u.uid = 'hanna_test_2@example.com'
AND NOT EXISTS (SELECT 1 FROM user_authorities ua WHERE ua.user_id = u.id AND ua.authority_id = a.id);

INSERT INTO documents (name, description, created_date, modified_date, creator_id)
SELECT 'Test_3', 'That is a test document', '2024-02-13T13:00:00', '2024-03-15T13:24:00', u.id
FROM users u
WHERE u.uid = 'hanna_test_2@example.com'
AND NOT EXISTS (SELECT 1 FROM documents d WHERE d.name = 'Test_3' AND d.creator_id = u.id);

INSERT INTO documents (name, description, created_date, modified_date, creator_id)
SELECT 'Test_4', 'That is a second test document', '2024-01-15T13:00:00', '2024-01-15T13:00:00', u.id
FROM users u
WHERE u.uid = 'hanna_test_2@example.com'
AND NOT EXISTS (SELECT 1 FROM documents d WHERE d.name = 'Test_4' AND d.creator_id = u.id);
