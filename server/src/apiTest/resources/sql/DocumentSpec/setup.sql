insert into document (name, description, creator_id )
select "Test_1", "That is a test document", u.id from user u where u.uid = "hanna_test_1";

insert into document (name, description, creator_id )
select "Test_2", "That is a second test document", u.id from user u where u.uid = "hanna_test_1";

insert into user (uid, name, password) values ( "hanna_test_2", "Hanna", "$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m" );

insert into document (name, description, creator_id )
select "Test_3", "That is a test document", u.id from user u where u.uid = "hanna_test_2";

insert into document (name, description, creator_id )
select "Test_4", "That is a second test document", u.id from user u where u.uid = "hanna_test_2";