INSERT IGNORE INTO documents (name, description, file_name, created_date, modified_date, creator_id)
SELECT "Test_1", "That is a test document", "test.txt", '2024-01-15 13:00:00', '2024-01-15 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";

INSERT IGNORE INTO documents (name, description, created_date, modified_date, creator_id)
SELECT "Test_2", "That is a second test document", '2024-01-15 16:00:00', '2024-01-15 16:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";

INSERT IGNORE INTO users (uid, name, password) VALUES ( "hanna_test_2@example.com", "Hanna", "$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m" );

INSERT IGNORE INTO user_authorities (user_id, authority_id)
SELECT u.id, a.id
FROM users u, authorities a
WHERE a.authority = 'ROLE_USER' AND u.uid = "hanna_test_2@example.com";

INSERT IGNORE INTO documents (name, description, created_date, modified_date, creator_id)
SELECT "Test_3", "That is a test document", '2024-02-13 13:00:00', '2024-03-15 13:24:00', u.id FROM users u WHERE u.uid = "hanna_test_2@example.com";

INSERT IGNORE INTO documents (name, description, created_date, modified_date, creator_id)
SELECT "Test_4", "That is a second test document", '2024-01-15 13:00:00', '2024-01-15 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_2@example.com";
