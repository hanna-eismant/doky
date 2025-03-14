INSERT IGNORE INTO documents (name, description, file_name, created_date, modified_date, creator_id)
SELECT "Test_1", "That is a test document", "test.txt", '2024-01-15 13:00:00', '2024-01-15 13:00:00', u.id FROM users u WHERE u.uid = "hanna_test_1@example.com";
