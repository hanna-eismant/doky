IF NOT EXISTS (SELECT * FROM users WHERE uid = 'hanna_test_1@example.com')
    INSERT INTO users (uid, name, password)
    VALUES ('hanna_test_1@example.com', 'Hanna', '$2a$10$bdZSuBncZqaM4XwHcjxbpeQuXeOxk6vCEsFrTwa91xh3M3JpvW41m');

INSERT INTO user_authorities(authority_id, user_id)
SELECT (SELECT id FROM authorities WHERE authority = 'ROLE_USER'), (SELECT id FROM users WHERE uid = 'hanna_test_1@example.com')
WHERE NOT EXISTS (SELECT 1 FROM user_authorities WHERE authority_id = (SELECT id FROM authorities WHERE authority = 'ROLE_USER') AND user_id = (SELECT id FROM users WHERE uid = 'hanna_test_1@example.com'))
