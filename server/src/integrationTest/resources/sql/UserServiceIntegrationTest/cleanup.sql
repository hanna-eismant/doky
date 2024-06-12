DELETE FROM user_authorities
WHERE user_id IN (SELECT id FROM users WHERE uid = 'hkurh.test.01@yopmail.com');

DELETE FROM users
WHERE uid = 'hkurh.test.01@yopmail.com';
