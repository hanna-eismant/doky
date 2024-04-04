delete d from document d inner join user u on d.creator_id = u.id where u.uid = "hanna_test_1@example.com";

delete u from user u where u.uid = "hanna_test_1@example.com";
