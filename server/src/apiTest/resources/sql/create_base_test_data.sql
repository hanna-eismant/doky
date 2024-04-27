INSERT IGNORE INTO user (uid, name, password) values ( "hanna_test_1@example.com", "Hanna", "$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m" );

INSERT IGNORE INTO user_authorities (user_id, authority_id)
SELECT u.id, a.id
FROM user u, authorities a
WHERE a.authority = 'ROLE_USER' AND u.uid = "hanna_test_1@example.com";
