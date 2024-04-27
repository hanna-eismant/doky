CREATE TABLE authorities
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    authority VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_authorities PRIMARY KEY (id)
);

CREATE TABLE user_authorities
(
    authority_id BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    CONSTRAINT pk_user_authorities PRIMARY KEY (authority_id, user_id)
);

ALTER TABLE authorities
    ADD CONSTRAINT uc_authorities_authority UNIQUE (authority);

ALTER TABLE user_authorities
    ADD CONSTRAINT fk_user_authorities_on_authority FOREIGN KEY (authority_id) REFERENCES authorities (id);

ALTER TABLE user_authorities
    ADD CONSTRAINT fk_user_authorities_on_user FOREIGN KEY (user_id) REFERENCES user (id);

CREATE INDEX idx_user_authorities_user_id ON user_authorities (user_id)
