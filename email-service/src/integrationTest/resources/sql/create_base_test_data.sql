INSERT IGNORE INTO users (uid, name, password) VALUES ("hanna_test_1@yopmail.com", "Hanna", "$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m");

INSERT IGNORE INTO user_authorities (user_id, authority_id)
SELECT u.id, a.id
FROM users u, authorities a
WHERE a.authority = 'ROLE_USER' AND u.uid = "hanna_test_1@yopmail.com"
