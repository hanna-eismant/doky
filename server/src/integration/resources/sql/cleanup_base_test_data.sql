delete d from document d inner join user u on d.creator_id = u.id where u.uid = "hanna.test";
delete u from user u where u.uid = "hanna.test";
