IF NOT EXISTS (
    SELECT 1 FROM users WHERE uid = 'hanna.kurhuzenkava@outlook.com'
)
BEGIN
    INSERT INTO users (uid, name, password)
    VALUES ('hanna.kurhuzenkava@outlook.com', 'Admin', '$2a$10$VFolreyF371093H1FJ28B.ugfQ0i/9EfZSZX0TTn.o9OGMJc1mK9K');
END;

IF NOT EXISTS (
    SELECT 1
    FROM user_authorities ua
    INNER JOIN users u ON u.id = ua.user_id
    INNER JOIN authorities a ON a.id = ua.authority_id
    WHERE u.uid = 'hanna.kurhuzenkava@outlook.com' AND a.authority = 'ROLE_ADMIN'
)
BEGIN
    INSERT INTO user_authorities (user_id, authority_id)
    SELECT u.id, a.id
    FROM users u
    INNER JOIN authorities a ON a.authority = 'ROLE_ADMIN'
    WHERE u.uid = 'hanna.kurhuzenkava@outlook.com';
END;
