insert into document (name, description, creator_id )
select "Test", "That is a test document", u.id from user u where u.uid = "hanna.test";

insert into document (name, description, creator_id )
select "Test2", "That is a second test document", u.id from user u where u.uid = "hanna.test";
