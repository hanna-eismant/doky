delete d from document d inner join user u on d.creator_id = u.id where u.uid = "hanna_test_1@example.com";

delete from reset_password_token;

delete a from user_authorities a inner join user u on a.user_id = u.id where u.uid = "hanna_test_1@example.com";

delete u from user u where u.uid = "hanna_test_1@example.com";
