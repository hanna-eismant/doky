delete u from user u where u.uid = "new_user_test@example.com";

delete a from user_authorities a inner join user u on a.user_id = u.id where u.uid = "new_user_test@example.com";
