DELETE d FROM documents d INNER JOIN users u ON d.creator_id = u.id WHERE u.uid = "hanna_test_1@example.com";

DELETE FROM reset_password_tokens;

DELETE a FROM user_authorities a INNER JOIN users u on a.user_id = u.id WHERE u.uid = "hanna_test_1@example.com";

DELETE u FROM users u WHERE u.uid = "hanna_test_1@example.com";
