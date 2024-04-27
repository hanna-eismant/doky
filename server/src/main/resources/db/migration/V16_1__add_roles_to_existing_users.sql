INSERT IGNORE INTO authorities (authority) VALUES ('ROLE_USER');
INSERT IGNORE INTO authorities (authority) VALUES ('ROLE_ADMIN');

INSERT IGNORE INTO user_authorities (user_id, authority_id)
SELECT u.id, a.id
FROM user u, authorities a
WHERE a.authority = 'ROLE_USER';
