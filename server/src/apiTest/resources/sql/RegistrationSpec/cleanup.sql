DELETE a FROM user_authorities a INNER JOIN users u ON a.user_id = u.id WHERE u.uid = "new_user_test@example.com";

DELETE u FROM users u WHERE u.uid = "new_user_test@example.com";
