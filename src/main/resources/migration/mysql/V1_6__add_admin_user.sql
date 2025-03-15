INSERT IGNORE INTO users (uid, name, password) VALUES ("hanna.kurhuzenkava@outlook.com", "Admin", "$2a$10$VFolreyF371093H1FJ28B.ugfQ0i/9EfZSZX0TTn.o9OGMJc1mK9K");

INSERT IGNORE INTO user_authorities (user_id, authority_id)
SELECT u.id, a.id
FROM users u, authorities a
WHERE a.authority = 'ROLE_ADMIN' AND u.uid = "hanna.kurhuzenkava@outlook.com";
